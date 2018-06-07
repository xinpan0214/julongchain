/*
 * Copyright Dingxuan. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package org.bcia.javachain.core.ledger;

import com.google.protobuf.ByteString;
import org.bcia.javachain.common.ledger.IResultsIterator;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.statedb.QueryResult;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.statedb.VersionedKV;
import org.bcia.javachain.core.ledger.kvledger.txmgmt.version.Height;
import org.bcia.javachain.core.ledger.ledgermgmt.LedgerManager;
import org.bcia.javachain.protos.ledger.queryresult.KvQueryResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类描述
 *
 * @author sunzongyu
 * @date 2018/05/31
 * @company Dingxuan
 */
public class QueryExecutorTest {
    IQueryExecutor queryExecutor = null;
    INodeLedger ledger = null;
    final String ns = "myGroup";
    final String coll = "coll";

    @Before
    public void before() throws Exception{
        LedgerManager.initialize(null);
        ledger = LedgerManager.openLedger(ns);
        queryExecutor = ledger.newQueryExecutor();
    }

    @Test
    public void testGetState() throws Exception{
        Assert.assertTrue(Arrays.equals(queryExecutor.getState(ns, "key"), "value pub".getBytes()));
    }

    @Test
    public void testGetStateMultipleKeys() throws Exception{
        Assert.assertTrue(Arrays.equals(queryExecutor.getStateMultipleKeys(ns, new ArrayList<String>(){{
            add("key");
        }}).get(0), "value pub".getBytes()));
    }

    @Test
    public void testGetStateRangeScanIterator() throws Exception{
        IResultsIterator itr = queryExecutor.getStateRangeScanIterator(ns, "ke", null);
        QueryResult n = itr.next();
        System.out.println(n);
        VersionedKV kv = (VersionedKV) n.getObj();
        Assert.assertEquals(kv.getCompositeKey().getKey(), "key");
        Assert.assertEquals(kv.getCompositeKey().getNamespace(), "myGroup");
        Assert.assertSame(kv.getVersionedValue().getVersion().getBlockNum(), (long) 1);
        Assert.assertSame(kv.getVersionedValue().getVersion().getTxNum(), (long) 0);
        Assert.assertTrue(Arrays.equals(kv.getVersionedValue().getValue(), "value pub".getBytes()));
    }

    @Test
    public void testGetPrivateData() throws Exception{
        byte[] privateData = queryExecutor.getPrivateData(ns, "coll", "key");
        Assert.assertTrue(Arrays.equals(privateData, "value pvt".getBytes()));
    }

    @Test
    public void testGetPrivateDataMultipleKeys() throws Exception{
        List<byte[]> privateDatas = queryExecutor.getPrivateDataMultipleKeys(ns, "coll", new ArrayList<String>() {{
            add("key");
        }});
        Assert.assertTrue(Arrays.equals(privateDatas.get(0), "value pvt".getBytes()));
    }

    @Test
    public void testGetPrivateDataRangeScanIterator() throws Exception{
        IResultsIterator itr = queryExecutor.getPrivateDataRangeScanIterator(ns, "coll", "key", "l");
        KvQueryResult.KV kv = (KvQueryResult.KV) itr.next().getObj();
        Assert.assertEquals(kv.getNamespace(), "myGroup");
        Assert.assertEquals(kv.getKey(), "key");
        Assert.assertEquals(kv.getValue(),ByteString.copyFromUtf8("value pvt"));
    }

    @After
    public void after() throws Exception{

    }
}