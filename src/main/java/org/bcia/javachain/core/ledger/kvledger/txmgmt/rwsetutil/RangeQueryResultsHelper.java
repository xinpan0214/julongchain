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
package org.bcia.javachain.core.ledger.kvledger.txmgmt.rwsetutil;

import org.bcia.javachain.common.exception.LedgerException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.protos.ledger.rwset.kvrwset.KvRwset;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author sunzongyu
 * @date 2018/04/18
 * @company Dingxuan
 */
public class RangeQueryResultsHelper {
    private static final JavaChainLog logger  = JavaChainLogFactory.getLog(RangeQueryResultsHelper.class);

    private List<KvRwset.KVRead> pendingResults;
    private MerkleTree mt;
    private int maxDegree;
    private boolean hashingEnable;

    public static RangeQueryResultsHelper newRangeQueryResultsHelper(boolean enableHashing, int maxDegree) throws LedgerException  {
        RangeQueryResultsHelper helper = new RangeQueryResultsHelper();
        helper.setHashingEnable(enableHashing);
        helper.setMaxDegree(maxDegree);
        if(enableHashing){
            helper.setMt(MerkleTree.newMerkleTree(maxDegree));
        }
        return helper;
    }

    public void addResult(KvRwset.KVRead kvRead){
        logger.debug("Adding a result");
        pendingResults.add(kvRead);
        if(hashingEnable && pendingResults.size() > maxDegree){
            logger.debug("Processing the accumulated results");
            processPendngResults();
        }
    }

    public KvRwset.QueryReadsMerkleSummary getMerkleSummary(){
        if(!hashingEnable){
            return null;
        }
        return mt.getSummery();
    }

    public void processPendngResults(){
        byte[] b = serializeKVReads(pendingResults);
        pendingResults.clear();
        //TODO getHash
        byte[] hash = "hash".getBytes();
        mt.update(hash);
    }

    private byte[] serializeKVReads(List<KvRwset.KVRead> list){
       return setKvReads(KvRwset.QueryReads.newBuilder(), list).build().toByteArray();
    }

    private KvRwset.QueryReads.Builder setKvReads(KvRwset.QueryReads.Builder builder, List<KvRwset.KVRead> list){
        for (int i = 0; i < list.size(); i++) {
            builder.setKvReads(i, list.get(i));
        }
        return builder;
    }

    public Map.Entry<List<KvRwset.KVRead>, KvRwset.QueryReadsMerkleSummary> done(){
        Map.Entry<List<KvRwset.KVRead>, KvRwset.QueryReadsMerkleSummary> entry = new AbstractMap.SimpleEntry<>(null, null);
        return entry;
    }

    public List<KvRwset.KVRead> getPendingResults() {
        return pendingResults;
    }

    public void setPendingResults(List<KvRwset.KVRead> pendingResults) {
        this.pendingResults = pendingResults;
    }

    public MerkleTree getMt() {
        return mt;
    }

    public void setMt(MerkleTree mt) {
        this.mt = mt;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public void setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
    }

    public boolean isHashingEnable() {
        return hashingEnable;
    }

    public void setHashingEnable(boolean hashingEnable) {
        this.hashingEnable = hashingEnable;
    }
}