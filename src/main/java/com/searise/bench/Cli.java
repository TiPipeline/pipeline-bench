package com.searise.bench;

import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import com.google.common.base.Preconditions;

public class Cli {
    public static void main(String []args) throws Exception {
        Properties props = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("./test.properties"));
        props.load(bufferedReader);

        DBHolder dbHolder = new DBHolder(props);
           
        Preconditions.checkArgument(args.length >= 1);
        String mode = StringUtils.trim(args[0]);
        switch (mode) {
            case "initialize":
                Initializer.run(props, dbHolder);
                break;
            case "test":
            {
                String sqlMode = "";
                if (args.length >= 2) {
                    sqlMode = StringUtils.trim(args[1]);
                }
                Tester.run(props, dbHolder, sqlMode);
                break;
            }
            default:
                throw new Exception("Unknown mode: " + mode);
        }
    }
}
