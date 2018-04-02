/**
 * Copyright DingXuan. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bcia.javachain.consenter;

import org.apache.commons.cli.ParseException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.consenter.common.cmd.IConsenterCmd;
import org.bcia.javachain.consenter.common.cmd.factory.ConsenterCmdFactory;
import org.bcia.javachain.consenter.util.Constant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhangmingyang
 * @Date: 2018/3/1 *
 * @company Dingxuan
 */
public class Consenter {
    private static JavaChainLog log = JavaChainLogFactory.getLog(Consenter.class);
    private IConsenterCmd iConsenterCmd;

    public void execCmd(String[] args) {
        if (args.length <= 0) {
            log.warn("Node command need more args-----");
            return;
        }
        int cmdWordCount;//记录命令单词数量
        String command = args[0];
        if (args.length == 1 && Constant.VERSION.equalsIgnoreCase(command)) {
            log.info("Consentor version is V0.25!..");
            iConsenterCmd = ConsenterCmdFactory.getInstance(command);
            String[] arg = new String[]{command};
            try {
                iConsenterCmd.execCmd(arg);
            } catch (org.apache.commons.cli.ParseException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1 && Constant.START.equalsIgnoreCase(command)) {
            iConsenterCmd = ConsenterCmdFactory.getInstance(command);
            String[] argment = new String[]{command};
            try {
                iConsenterCmd.execCmd(argment);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            log.info("args is error!");
        }
        return;
    }

    public static void main(String[] args) {
        //引入Spring配置文件
  //     ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Consenter consenter = new Consenter();
        String[] start = new String[]{"start"};
        consenter.execCmd(start);
    }

}

