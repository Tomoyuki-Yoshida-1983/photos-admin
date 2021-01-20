package jp.yoshida.photos_admin.common.constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 基準定数
 */
public class StandardsConstants {

    public static final String JAVA_IO_TMPDIR;

    public static final String TMPDIR_PREFIX = "p-a-";

    public static final String TMPDIR_SUFFIX = ".tmp";

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT;

    static {

        String tmpdir = System.getProperty("java.io.tmpdir");

        // WindowsとLinuxで一時ファイルディレクトリの末尾の区切り文字の有無が異なるため、差異を吸収する。
        if (tmpdir.endsWith(File.separator)) {
            tmpdir = tmpdir.substring(0, tmpdir.length() - 1);
        }

        JAVA_IO_TMPDIR = tmpdir;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT"));
        SIMPLE_DATE_FORMAT = sdf;
    }
}
