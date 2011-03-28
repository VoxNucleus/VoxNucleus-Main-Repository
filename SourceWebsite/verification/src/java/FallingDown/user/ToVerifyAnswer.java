package FallingDown.user;

/**
 *
 * @author victork
 */
public class ToVerifyAnswer {

    private transient ToVerify thingsToVerify;

    private boolean isUsernameCorrect=true;

    public void attachToVerify(ToVerify verif){
        thingsToVerify=verif;
    }

        public void launchVerification(){
        isUsernameCorrect=UsernameVerificator.verifyUsername(thingsToVerify.getUsername());
    }

}
