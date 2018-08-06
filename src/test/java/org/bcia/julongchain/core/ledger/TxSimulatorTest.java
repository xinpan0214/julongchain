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
package org.bcia.julongchain.core.ledger;

import com.google.protobuf.ByteString;
import org.bcia.julongchain.common.exception.LedgerException;
import org.bcia.julongchain.common.ledger.IResultsIterator;
import org.bcia.julongchain.core.ledger.kvledger.txmgmt.rwsetutil.TxRwSet;
import org.bcia.julongchain.core.ledger.kvledger.txmgmt.statedb.QueryResult;
import org.bcia.julongchain.core.ledger.kvledger.txmgmt.statedb.VersionedKV;
import org.bcia.julongchain.core.ledger.ledgermgmt.LedgerManager;
import org.bcia.julongchain.protos.ledger.queryresult.KvQueryResult;
import org.bcia.julongchain.protos.ledger.rwset.Rwset;
import org.bcia.julongchain.protos.ledger.rwset.kvrwset.KvRwset;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 类描述
 *
 * @author sunzongyu
 * @date 2018/04/24
 * @company Dingxuan
 */
public class TxSimulatorTest {
    ITxSimulator simulator = null;
    INodeLedger ledger = null;
    TxSimulationResults txSimulationResults = null;
    final String ledgerID = "myGroup";
    final String ns = "jdoe-voucher";

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void before() throws LedgerException  {
        LedgerManager.initialize(null);
        ledger = LedgerManager.openLedger(ledgerID);
        simulator = ledger.newTxSimulator("5");
    }

    @Test
    public void testSetState() throws Exception {
    	expectedEx.expect(LedgerException.class);
    	expectedEx.expectMessage("This instance should not be used after calling Done()");
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        simulator.setState(ledgerID, "key", "test set state".getBytes());
        simulator.setState(ledgerID, "key1", "test set state".getBytes());
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPublicReadWriteSet().getNsRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertEquals(kvRWSet.getWrites(1).getKey(), "key1");
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "test set state");
        Assert.assertEquals(kvRWSet.getWrites(1).getValue().toStringUtf8(), "test set state");
        simulator.setState(ledgerID + "1", "key", "test set state".getBytes());
    }

    @Test
    public void testDeleteState() throws Exception{
	    expectedEx.expect(LedgerException.class);
	    expectedEx.expectMessage("This instance should not be used after calling Done()");
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        simulator.deleteState(ledgerID, "key");
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPublicReadWriteSet().getNsRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertTrue(kvRWSet.getWrites(0).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "");
        simulator.setState(ledgerID, "key", "test set state".getBytes());
    }

    @Test
    public void testSetStateMulitipleKeys() throws Exception{
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        simulator.setStateMultipleKeys(ledgerID, new HashMap<String, byte[]>(){{
            put("key1", "test set state1".getBytes());
            put("key2", "test set state2".getBytes());
            put("key3", "test set state3".getBytes());
            put("key4", "test set state4".getBytes());
            put("key5", "test set state5".getBytes());
            put("key6", "test set state6".getBytes());
        }});
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPublicReadWriteSet().getNsRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        int i = 0;
        for(KvRwset.KVWrite write : kvRWSet.getWritesList()){
            int j = ++i;
            Assert.assertEquals(write.getKey(), "key" + j);
            Assert.assertFalse(write.getIsDelete());
            Assert.assertEquals(write.getValue().toStringUtf8(), "test set state" + j);
        }
    }

    /*-------------------------------------------------------------------
    @Test
    public void testsetPrivateData() throws Exception{
	    expectedEx.expect(LedgerException.class);
	    expectedEx.expectMessage("This instance should not be used after calling Done()");
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        Rwset.CollectionPvtReadWriteSet CollRWSet = null;
        simulator.setPrivateData(ns, coll, "key", "test set private data".getBytes());
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertFalse(kvRWSet.getWrites(0).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "test set private data");
        simulator.setPrivateData(ns, coll, "key1", "test set private data1".getBytes());
    }

    @Test
    public void testSetPirvateDataMultipleKeys() throws Exception{
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        Rwset.CollectionPvtReadWriteSet CollRWSet = null;
        simulator.setPrivateData(ns, coll, "key", "test set private data".getBytes());
        simulator.setPrivateData(ns, coll, "key1", "test set private data1".getBytes());
        simulator.setPrivateData(ns, coll + "1", "key1", "test set private data1".getBytes());
        simulator.setPirvateDataMultipleKeys(ns, coll + "2", new HashMap<String, byte[]>(){{
            put("key1", "test set private data mulitiple keys1".getBytes());
            put("key2", "test set private data mulitiple keys2".getBytes());
            put("key3", "test set private data mulitiple keys3".getBytes());
            put("key4", "test set private data mulitiple keys4".getBytes());
            put("key5", "test set private data mulitiple keys5".getBytes());
            put("key6", "test set private data mulitiple keys6".getBytes());

        }});
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(2).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        int i = 0;
        for(KvRwset.KVWrite write : kvRWSet.getWritesList()){
            int j = ++i;
            Assert.assertEquals(write.getKey(), "key" + j);
            Assert.assertFalse(write.getIsDelete());
            Assert.assertEquals(write.getValue().toStringUtf8(), "test set private data mulitiple keys" + j);
        }
    }

    @Test
    public void testDeletePrivateData() throws Exception{
	    expectedEx.expect(LedgerException.class);
	    expectedEx.expectMessage("This instance should not be used after calling Done()");
        ByteString rwset = null;
        KvRwset.KVRWSet kvRWSet = null;
        Rwset.CollectionPvtReadWriteSet CollRWSet = null;
        simulator.deletePrivateData(ns, coll, "key");
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertTrue(kvRWSet.getWrites(0).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "");
        simulator.setPrivateData(ns, coll, "key", "test set private data".getBytes());
        simulator.deletePrivateData(ns, coll, "key");
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertTrue(kvRWSet.getWrites(0).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "");
        simulator.setPrivateData(ns, coll, "key", "test set private data".getBytes());
        simulator.setPrivateData(ns, coll, "key1", "test set private data1".getBytes());
        simulator.deletePrivateData(ns, coll, "key");
        txSimulationResults = simulator.getTxSimulationResults();
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(0).getKey(), "key");
        Assert.assertTrue(kvRWSet.getWrites(0).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(0).getValue().toStringUtf8(), "");
        rwset = txSimulationResults.getPrivateReadWriteSet().getNsPvtRwset(0).getCollectionPvtRwset(0).getRwset();
        kvRWSet = KvRwset.KVRWSet.parseFrom(rwset);
        Assert.assertEquals(kvRWSet.getWrites(1).getKey(), "key1");
        Assert.assertFalse(kvRWSet.getWrites(1).getIsDelete());
        Assert.assertEquals(kvRWSet.getWrites(1).getValue().toStringUtf8(), "test set private data1");
    }
    -------------------------------------------------------------------*/

    @Test
    public void testGetState() throws Exception {
		byte[] as = simulator.getState(ns, "a");
		Assert.assertEquals("10", new String(as));
		byte[] cs = simulator.getState(ns, "c");
		Assert.assertEquals("300", new String(cs));
	}

	@Test
	public void testGetStateMultipleKeys() throws Exception{
		List<byte[]> states = simulator.getStateMultipleKeys(ns, new ArrayList<String>() {{
			add("a");
			add("b");
			add("c");
		}});
		states.forEach((b) -> {
			Assert.assertNotNull(b);
		});
	}

	@Test
	public void testGetStateRangeScanIterator() throws Exception{
    	simulator.getState(ns, "a");
    	simulator.setState(ns, "a1", "haha".getBytes());
		IResultsIterator itr = simulator.getStateRangeScanIterator(ns, "a", "b");
		System.out.println(itr.next());
//		for (int i = 0; i < 6; i++) {
//			QueryResult n = itr.next();
//			VersionedKV kv = (VersionedKV) n.getObj();
//			System.out.println(kv.getCompositeKey().getKey());
//			System.out.println(new String(kv.getVersionedValue().getValue()));
//		}
		TxSimulationResults txSimulationResults = simulator.getTxSimulationResults();
		Rwset.TxReadWriteSet publicReadWriteSet = txSimulationResults.getPublicReadWriteSet();
		TxRwSet txRwSet = new TxRwSet();
		txRwSet.fromProtoBytes(publicReadWriteSet.getNsRwset(0).getRwset());
		System.out.println(publicReadWriteSet);
	}

	/*-------------------------------------------------------------

	@Test
	public void testGetPrivateData() throws Exception{
		for (int i = 0; i < 6; i++) {
			byte[] privateData = simulator.getPrivateData(ledgerID, "coll", "key" + i);
			Assert.assertTrue(Arrays.equals(privateData, ("pvt value" + i).getBytes()));
		}
	}

	@Test
	public void testGetPrivateDataMultipleKeys() throws Exception{
		List<byte[]> privateDatas = simulator.getPrivateDataMultipleKeys(ledgerID, "coll", new ArrayList<String>() {{
			add("key0");
			add("key1");0] = (byte) 0;
			add("key2");
			add("key3");
			add("key4");
			add("key5");
		}});
		for (int i = 0; i < 6; i++) {
			Assert.assertTrue(Arrays.equals(privateDatas.get(i), ("pvt value" + i).getBytes()));
		}
	}

	@Test
	public void testGetPrivateDataRangeScanIterator() throws Exception{
		IResultsIterator itr = simulator.getPrivateDataRangeScanIterator(ledgerID, "coll", "key", "l");
		for (int i = 0; i < 6; i++) {
			QueryResult qr = itr.next();
			KvQueryResult.KV kv = ((KvQueryResult.KV) qr.getObj());
			Assert.assertEquals(kv.getNamespace(), ledgerID);
			Assert.assertEquals(kv.getKey(), "key" + i);
			Assert.assertEquals(kv.getValue(),ByteString.copyFromUtf8("pvt value" + i));
		}
	}

	-------------------------------------------------------------*/

    private static void soutBytes(byte[] bytes){
        int i = 0;
        for(byte b : bytes){
            System.out.print(b + " ");
        }
    }
}


