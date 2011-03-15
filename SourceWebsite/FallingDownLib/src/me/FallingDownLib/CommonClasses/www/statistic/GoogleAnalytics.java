package me.FallingDownLib.CommonClasses.www.statistic;

/**
 *
 * @author victork
 */
public class GoogleAnalytics {

    private static final String ANALYTICS_CODE = "<script type=\"text/javascript\">"
            + "var _gaq = _gaq || [];"
            + "_gaq.push(['_setAccount', 'UA-15978628-2']);"
            + "_gaq.push(['_setDomainName', 'none']);"
            + "_gaq.push(['_setAllowLinker', true]);"
            + "_gaq.push(['_trackPageview']);"
            + "(function() {"
            + "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;"
            + "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';"
            + "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);"
            + "})();"
            + "</script>";

    public static String getAnalyticsCode(){
        return ANALYTICS_CODE;
    }

}
