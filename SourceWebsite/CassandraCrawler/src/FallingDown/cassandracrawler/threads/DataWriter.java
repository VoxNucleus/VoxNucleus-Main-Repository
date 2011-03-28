package FallingDown.cassandracrawler.threads;

import FallingDown.cassandracrawler.ConnectionToSolR;
import FallingDown.cassandracrawler.DatabaseRetriever;
import FallingDown.cassandracrawler.document.CommentToDocument;
import FallingDown.cassandracrawler.document.PostToDocument;
import FallingDown.cassandracrawler.document.UserToDocument;
import FallingDown.cassandracrawler.cassandra.ToArchive;
import FallingDown.cassandracrawler.logger.CrawlerLogger;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.FallingDownLib.CassandraConnection.common.ColumnFamilies;
import me.FallingDownLib.CommonClasses.Comment;
import me.FallingDownLib.CommonClasses.CommentHash;
import me.FallingDownLib.CommonClasses.Exceptions.PostDoesNotExist;
import me.FallingDownLib.CommonClasses.Post;
import me.FallingDownLib.CommonClasses.PostHash;
import me.FallingDownLib.CommonClasses.User;
import me.FallingDownLib.CommonClasses.UserHash;
import me.FallingDownLib.CommonClasses.util.EasyUUIDget;
import me.FallingDownLib.CommonClasses.util.GarbageCollector;
import me.prettyprint.cassandra.service.PoolExhaustedException;
import me.prettyprint.cassandra.utils.StringUtils;
import org.apache.cassandra.thrift.Column;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.thrift.TException;

/**
 *
 * @author victork
 */
public class DataWriter extends Thread {
    //One pear hour this thread is awaken;
    private long DATAWRITER_SLEEPTIME=1000*3600;
    private SolrServer server=null;

    public static final int CASE_POST=0;
    public static final int CASE_COMMENT=1;
    public static final int CASE_USER=2;

    public DataWriter(String name){
        super(name);
    }

    @Override
    public void run(){
        for (;;) {
            try {
                server = ConnectionToSolR.openConnectionToSolR();
                launchCreatedNotIndexed();
                launchDeletedNotIndexed();
            } catch (MalformedURLException ex) {
                System.out.println("The address used seems to do not work");
            }

            goToSleep();
        }

    }

    /**
     * Function that is made in order for the thread to go to sleep for a period of time (given in the
     * constructor of the class)
     */
    private void goToSleep() {
        try {
            DataWriter.sleep(DATAWRITER_SLEEPTIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Begin indexing newly created items but not yet indexed
     */

    private void launchCreatedNotIndexed(){

         DatabaseRetriever dataToIndex = new DatabaseRetriever();
            try {
                dataToIndex.retrieve(DatabaseRetriever.DB_CREATED_NOT_INDEXED);
                doIndexPosts(dataToIndex.getPostList(),DatabaseRetriever.DB_CREATED_NOT_INDEXED);
                doIndexUsers(dataToIndex.getUserList());
                doIndexComments(dataToIndex.getCommentList());
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    /**
     * Todo : delete from SolR
     */

    private void launchDeletedNotIndexed(){

        DatabaseRetriever dataToIndex = new DatabaseRetriever();
            try {
                dataToIndex.retrieve(DatabaseRetriever.DB_DELETED_NOT_INDEXED);
                doDeleteIds(dataToIndex.getPostList(),DatabaseRetriever.DB_DELETED_NOT_INDEXED,
                        ToArchive.IS_A_POST);
                doDeleteIds(dataToIndex.getUserList(),DatabaseRetriever.DB_DELETED_NOT_INDEXED,
                        ToArchive.IS_A_USER);
                doDeleteIds(dataToIndex.getCommentList(),DatabaseRetriever.DB_DELETED_NOT_INDEXED,
                        ToArchive.IS_A_COMMENT);
            } catch (PoolExhaustedException ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    /**
     * TODO
     */

    private void launchModifiedNotIndexed(){
        DatabaseRetriever dataToIndex = new DatabaseRetriever();
            try {
                dataToIndex.retrieve(DatabaseRetriever.DB_DELETED_NOT_INDEXED);
                doIndexPosts(dataToIndex.getPostList(),DatabaseRetriever.DB_MODIFIED_NOT_INDEXED);
                doIndexUsers(dataToIndex.getUserList());
                doIndexComments(dataToIndex.getCommentList());
            } catch (PoolExhaustedException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (TException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (Exception ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
    }


    /**
     * Index posts
     * @param list
     */
    private void doIndexPosts(ArrayList<Column> list, String columnFamily){
        int nbIndexedPost=0;
        ToArchive toArchive = new ToArchive();
        toArchive.setColumnFamily(ColumnFamilies.DB_INDEX_CREATED);
        ArrayList<Column> toDelete = new ArrayList<Column>();
        GarbageCollector garbage_collector = new GarbageCollector();
        for(int indexCol=list.size()-1;indexCol>=0;indexCol--)
        {
            try {
                PostHash post = new PostHash(
                        Post.getPostFromDatabase(
                        StringUtils.string(
                        list.get(indexCol).getValue())));
                PostToDocument post2Doc= new PostToDocument(post);
                server.add(post2Doc.getDocument());
                server.commit();
                toArchive.addToArchive(list.get(indexCol),ToArchive.IS_A_POST);
                toDelete.add(list.get(indexCol));
                nbIndexedPost++;
            } catch (PoolExhaustedException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (PostDoesNotExist ex) {
                garbage_collector.addColumnToDelete(columnFamily, "Posts",list.get(indexCol).getName());
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (TException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (Exception ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
        }
        CrawlerLogger.getInstance().insertInfo(nbIndexedPost + " noyaux ont été indexés");
        toArchive.flushToArchive();
        garbage_collector.flushGarbage(null);
    }

    /**
     * Index users
     * @param list
     */
    private void doIndexUsers(ArrayList<Column> list) {
        int nbIndexedUser = 0;
        ToArchive toArchive = new ToArchive();
        toArchive.setColumnFamily(ColumnFamilies.DB_INDEX_CREATED);
        for (int indexCol = list.size() - 1; indexCol >= 0; indexCol--) {
            try {
                UserHash user = new UserHash(
                        User.getUserFromDatabase(
                        StringUtils.string(
                        list.get(indexCol).getValue())));
                UserToDocument post2Doc = new UserToDocument(user);
                server.add(post2Doc.getDocument());
                server.commit();
                toArchive.addToArchive(list.get(indexCol), ToArchive.IS_A_USER);
                nbIndexedUser++;
            } catch (PoolExhaustedException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (TException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (Exception ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
        }
        CrawlerLogger.getInstance().insertInfo(nbIndexedUser + " comptes ont été indexés");
        toArchive.flushToArchive();
    }

    /**
     * Index comments
     * @param list
     */
    private void doIndexComments(ArrayList<Column> list) {
        int nbIndexedComment=0;
        ToArchive toArchive = new ToArchive();
        toArchive.setColumnFamily(ColumnFamilies.DB_INDEX_CREATED);
        for(int indexCol=list.size()-1;indexCol>=0;indexCol--)
        {
            try {
                CommentHash comment = new CommentHash(
                        Comment.getCommentFromDatabase(
                        StringUtils.string(list.get(indexCol).getValue()),
                        list.get(indexCol).getName() ));
                CommentToDocument comment2Doc= new CommentToDocument(comment,
                        list.get(indexCol).getName());
                server.add(comment2Doc.getDocument());
                server.commit();
                toArchive.addToArchive(list.get(indexCol),ToArchive.IS_A_COMMENT);
                nbIndexedComment++;
            } catch (PoolExhaustedException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (TException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (Exception ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
        }
        CrawlerLogger.getInstance().insertInfo(nbIndexedComment + " commentaires ont été indexés");
        toArchive.flushToArchive();
    }

    /**
     * 
     * @param list
     * @param columnFamily
     * @param type type of the column that must be deleted
     */
    private void doDeleteIds(ArrayList<Column> list, String columnFamily, int type) {
        int nbDeleted=0;
        ToArchive toArchive = new ToArchive();
        toArchive.setColumnFamily(ColumnFamilies.DB_INDEX_DELETED);
        for (int indexCol = 0; indexCol < list.size(); indexCol++) {
            try {
                server.deleteByQuery("uuid:" + EasyUUIDget.toUUID(list.get(indexCol).getName()).toString());
                toArchive.addToArchive(list.get(indexCol), type);
                server.commit();
                nbDeleted++;
            } catch (SolrServerException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            } catch (IOException ex) {
                CrawlerLogger.getInstance().insertError(ex.toString());
            }
        }
        CrawlerLogger.getInstance().insertInfo(nbDeleted+" éléments ont été supprimés");
        toArchive.flushToArchive();
    }

    /**
     * TODO
     * @param docs
     */

    private void addAndCommit(Collection<SolrInputDocument> docs){
        try {
            server.add(docs);
            server.commit();
        } catch (SolrServerException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
