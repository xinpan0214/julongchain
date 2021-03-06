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
package org.bcia.julongchain.core.ledger.kvledger.txmgmt.rwsetutil;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.ArrayUtils;
import org.bcia.julongchain.common.exception.LedgerException;
import org.bcia.julongchain.common.log.JulongChainLog;
import org.bcia.julongchain.common.log.JulongChainLogFactory;
import org.bcia.julongchain.core.ledger.ledgerconfig.LedgerConfig;
import org.bcia.julongchain.core.ledger.util.Util;
import org.bcia.julongchain.protos.ledger.rwset.kvrwset.KvRwset;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 莫尔克树
 *
 * @author sunzongyu
 * @date 2018/04/18
 * @company Dingxuan
 */
public class MerkleTree {
    private static JulongChainLog log = JulongChainLogFactory.getLog(MerkleTree.class);
    private static final int LEAF_LEVEL = 1;
    private static final int MIN_TREE_LEVEL = 2;

    private Map<Integer, List<byte[]>> tree;
    private int maxLevel;
    private int maxDegree;

    public MerkleTree(int maxDegree) {
        if(maxDegree < MIN_TREE_LEVEL){
        	this.maxDegree = LedgerConfig.getMaxDegreeQueryReadsHashing();
        } else {
			this.maxDegree = maxDegree;
		}
        this.tree = new HashMap<>();
        this.tree.put(LEAF_LEVEL, new ArrayList<>());
        this.maxLevel = 1;
    }

    public void update(byte[] nextLeafLevelHash) throws LedgerException{
        log.debug("Before update. Tree's max level is " + tree.size());
        List<byte[]> leafLevelHash = tree.computeIfAbsent(LEAF_LEVEL, k -> new ArrayList<>());
        leafLevelHash.add(nextLeafLevelHash);
        for (int currentLelvel = LEAF_LEVEL;; currentLelvel++) {
			List<byte[]> currenLevelHashes = tree.computeIfAbsent(currentLelvel, k -> new ArrayList<>());
			if(currenLevelHashes.size() < maxDegree){
                log.debug("After update. Tree's max level is " + tree.size());
                return;
            }
            byte[] nextLevelHash = computeCombinedHash(currenLevelHashes);
            tree.put(currentLelvel, new ArrayList<>());
            int nextLevel = currentLelvel + 1;

			List<byte[]> nextLevelHashes = tree.computeIfAbsent(nextLevel, k -> new ArrayList<>());
			nextLevelHashes.add(nextLevelHash);
            if(nextLevel > maxLevel){
                maxLevel = nextLevel;
            }
            currentLelvel = nextLevel;
        }
    }

    public void done() throws LedgerException{
        log.debug("Before done.");
        int currentLevel = LEAF_LEVEL;
        byte[] hash = null;
        while(currentLevel < maxLevel){
            List<byte[]> currentLevelHashes = tree.get(currentLevel);
			if (currentLevelHashes == null) {
				continue;
			}
			if (currentLevelHashes.size() == 0) {
				currentLevel++;
				continue;
			} else {
				hash = computeCombinedHash(currentLevelHashes);
			}
            tree.remove(currentLevel++);
            tree.get(currentLevel).add(hash);
        }
        List<byte[]> finalHash = tree.get(maxLevel);
        if(finalHash.size() > maxDegree){
           tree.remove(maxLevel);
           maxLevel++;
           byte[] combinedHash = computeCombinedHash(finalHash);
           List<byte[]> l = new ArrayList<>();
           l.add(combinedHash);
           tree.put(maxLevel, l);
        }
        log.debug("After done.");
    }

    public KvRwset.QueryReadsMerkleSummary getSummery(){
        List<ByteString> list = new ArrayList<>();
		List<byte[]> maxLevelHashes = getMaxLevelHashes();
		for (byte[] bytes : maxLevelHashes) {
			list.add(ByteString.copyFrom(bytes));
		}
		return KvRwset.QueryReadsMerkleSummary.newBuilder()
				.addAllMaxLevelHashes(list)
				.setMaxDegree(maxDegree)
				.setMaxLevel(maxLevel)
				.build();
    }

    public List<byte[]> getMaxLevelHashes(){
        return tree.get(maxLevel);
    }

    public byte[] getRootHash() throws LedgerException {
    	return computeCombinedHash(getMaxLevelHashes());
	}

    public boolean isEmpty(){
        return maxLevel == 1 && tree.get(maxLevel).size() == 0;
    }

    @Override
    public String toString() {
        return "tree" + tree;
    }

    public static byte[] computeCombinedHash(List<byte[]> hashes) throws LedgerException{
        byte[] combinedHash = new byte[]{};
        for(byte[] h : hashes){
			if (h == null) {
				continue;
			}
            combinedHash = ArrayUtils.addAll(combinedHash, h);
        }
        return combinedHash.length == 0 ? new byte[]{} : Util.getHashBytes(combinedHash);
    }

    public Map<Integer, List<byte[]>> getTree() {
        return tree;
    }

    public void setTree(Map<Integer, List<byte[]>> tree) {
        this.tree = tree;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public void setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
    }

	public static void main(String[] args) throws Exception {
		MerkleTree tree = new MerkleTree(2);
//		tree.update("1".getBytes(StandardCharsets.UTF_8));
//		tree.update("2".getBytes(StandardCharsets.UTF_8));
//		tree.update("3".getBytes(StandardCharsets.UTF_8));
//		tree.update("4".getBytes(StandardCharsets.UTF_8));
//		tree.update("5".getBytes(StandardCharsets.UTF_8));
//		tree.update("6".getBytes(StandardCharsets.UTF_8));
//		tree.update("7".getBytes(StandardCharsets.UTF_8));
//		tree.update("8".getBytes(StandardCharsets.UTF_8));
//		tree.update("9".getBytes(StandardCharsets.UTF_8));
		tree.done();
		System.out.println(Hex.toHexString(tree.getRootHash()) == null);
		KvRwset.QueryReadsMerkleSummary summery = tree.getSummery();
		System.out.println(summery);
	}
}
