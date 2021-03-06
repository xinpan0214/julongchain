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
package org.bcia.julongchain.consenter.util;

import com.google.protobuf.ByteString;
import org.bcia.julongchain.common.exception.JulongChainException;
import org.bcia.julongchain.common.log.JulongChainLog;
import org.bcia.julongchain.common.log.JulongChainLogFactory;
import org.bcia.julongchain.csp.gm.dxct.sm3.SM3HashOpts;
import org.bcia.julongchain.protos.common.Common;

import static org.bcia.julongchain.csp.factory.CspManager.getDefaultCsp;

/**
 * 区块辅助类
 *
 * @author zhangmingyang
 * @Date: 2018/5/10
 * @company Dingxuan
 */
public class BlockHelper {
    private static JulongChainLog log = JulongChainLogFactory.getLog(BlockHelper.class);
    private byte[] previousHash;
    private byte[] dataHash;
    private long number;


    public BlockHelper(byte[] previousHash, byte[] dataHash, long number) {
        this.previousHash = previousHash;
        this.dataHash = dataHash;
        this.number = number;
    }

    public static Common.Block createBlock(long seqNum, byte[] previousHash) {
        Common.Block.Builder block = Common.Block.newBuilder();
        //log.info(String.format("This Block's Num is %s", seqNum));

        Common.BlockData.Builder blockData = Common.BlockData.newBuilder();
        Common.BlockMetadata.Builder metaData = Common.BlockMetadata.newBuilder();

        for (int i = 0; i < ConsenterConstants.METADATA_SIZE; i++) {
            block.getMetadataBuilder().addMetadata(ByteString.copyFrom(metaData.build().toByteArray()));
        }
        block.getHeaderBuilder().setNumber(seqNum).setPreviousHash(ByteString.copyFrom(previousHash));
        block.setData(blockData);
        return block.build();
    }


    public static byte[] hash(byte[] data) {
        byte[] digest = new byte[0];
        try {
            digest = getDefaultCsp().hash(data, new SM3HashOpts());
        } catch (JulongChainException e) {
            e.printStackTrace();
        }
        return digest;
    }

    public byte[] getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(byte[] previousHash) {
        this.previousHash = previousHash;
    }

    public byte[] getDataHash() {
        return dataHash;
    }

    public void setDataHash(byte[] dataHash) {
        this.dataHash = dataHash;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
