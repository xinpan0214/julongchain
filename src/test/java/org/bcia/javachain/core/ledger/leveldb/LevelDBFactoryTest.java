package org.bcia.javachain.core.ledger.leveldb;

import org.apache.commons.lang3.ArrayUtils;
import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.core.ledger.util.Util;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.Snapshot;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.iq80.leveldb.impl.SeekingIteratorAdapter;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * LevelDBFactoryTest测试类
 *
 * @author
 * @date 2018/3/12
 * @company Dingxuan
 */
public class LevelDBFactoryTest {

    private static JavaChainLog log = JavaChainLogFactory.getLog(LevelDBFactoryTest.class);

    @Test
    public void getDB() throws LedgerException {
        DB db = LevelDBFactory.getDB();
        Assert.assertNotNull(db);
        LevelDBFactory.closeDB(db);
    }

    @Test
    public void closeDB() throws IOException {
//        DB db = LevelDBFactory.getDB();
//        LevelDBFactory.closeDB(db);
    }

    @Test
    public void add() throws LedgerException {
//        String key = "key-001";
//        String value = "value-001["+ UUID.randomUUID().toString() + "]" ;
//        log.info("add key");
//        LevelDBFactory.add(key.getBytes(Charset.forName("utf-8")), value.getBytes(Charset.forName("utf-8")), true);
//        log.info("query key");
//        byte[] bytes = LevelDBFactory.get(key.getBytes(Charset.forName("utf-8")), false);
//        String query = new String(bytes);
//        log.info("original value:" + value);
//        log.info("query value:" + query);
//        Assert.assertEquals(value, query);

        for (int i = 0; i < 100; i++) {
            byte[] key = String.valueOf(i).getBytes();
            byte[] value1 = Util.longToBytes(1000 + i, 8);
            byte[] value2 = Util.longToBytes(10000 + i, 8);
            byte[] value = ArrayUtils.addAll(value1, value2);
            LevelDBFactory.add(key, value, true);
//            closeDB();
        }
        byte[] key = {0x00};
        LevelDBFactory.add(key, "askjaklj".getBytes(), true);
//        for (int i = 0; i < 100; i++) {
//            String path = "/home/bcia/leveldb";
//            DB db = Iq80DBFactory.factory.open(new File(path), new Options().createIfMissing(true));
//        }


    }

    @Test
    public void delete() throws LedgerException {
        String key = UUID.randomUUID().toString();
        log.info("key:" + key);
        String value = UUID.randomUUID().toString();
        log.info("value:" + value);
        LevelDBFactory.add(key.getBytes(Charset.forName("utf-8")), value.getBytes(Charset.forName("utf-8")), true);
        log.info("add to level db");
        Assert.assertEquals(value, new String(LevelDBFactory.get(key.getBytes(Charset.forName("utf-8")), false)));
        LevelDBFactory.delete(key.getBytes(Charset.forName("utf-8")), true);
        Assert.assertNull(LevelDBFactory.get(key.getBytes(Charset.forName("utf-8")), false));
    }

    @Test
    public void get() throws LedgerException {
//        log.info(new String(LevelDBFactory.get("key-001".getBytes(Charset.forName("utf-8")))));
//        String key = UUID.randomUUID().toString();
//        log.info("key:" + key);
//        String value = UUID.randomUUID().toString();
//        log.info("value:" + value);
//        LevelDBFactory.add(key.getBytes(Charset.forName("utf-8")), value.getBytes(Charset.forName("utf-8")), true);
//        log.info("add to level db");
//        byte[] queryBytes = LevelDBFactory.get(key.getBytes(Charset.forName("utf-8")), false);
//        String queryString = new String(queryBytes);
//        log.info("query key from level db");
//        log.info("query string:" + queryString);
//        Assert.assertEquals(value, queryString);
        byte[] key = "underConstructionLedgerKey".getBytes();
        byte[] value = LevelDBFactory.get(key, true);
        System.out.println(new String(value));
        byte[] value1 = LevelDBFactory.get(key, true);
//        System.out.println(new String(value1));

    }

    @Test
    public void getIterator() throws LedgerException {
//        try {
//            DB db = LevelDBFactory.getDB();
////            LevelDBFactory.add(db, "123".getBytes(), "OneTwoThree".getBytes(), true);
////            LevelDBFactory.add(db, "456".getBytes(), "FourFiveSix".getBytes(), true);
////            LevelDBFactory.add(db, "789".getBytes(), "SevenEightNine".getBytes(), true);
//            DBIterator iterator = LevelDBFactory.getIterator(db);
//            while(iterator.hasNext()){
//                String key = new String(iterator.next().getKey());
////                System.out.println(key.compareTo("123"));
//                Map.Entry<byte[], byte[]> entry = iterator.next();
//                System.out.println(new String(entry.getKey()));
//                System.out.println(new String(entry.getValue()));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//       DBIterator itr = LevelDBFactory.getIterator();
//       byte[] startKey = {0x00};
//
//       while(itr.hasNext()){
//           Map.Entry<byte[], byte[]> entry = itr.next();
//           String s = new String(entry.getKey());
//           if(String.){
//
//           }
//           for(byte b : entry.getKey()){
//               System.out.print(b + " ");
//           }
//           System.out.println();
//           System.out.println(entry.getValue());
//       }
    }

    @Test
    public void test() throws Throwable {
        DB db = LevelDBFactory.getDB();
        byte[] key = {0x00};
        LevelDBFactory.get(key, false);
        LevelDBFactory.get(key, false);
    }
}