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
package org.bcia.javachain.msp.gmsoft;

import org.bcia.javachain.common.exception.JavaChainException;
import org.bcia.javachain.common.log.JavaChainLog;
import org.bcia.javachain.common.log.JavaChainLogFactory;
import org.bcia.javachain.csp.gm.sm2.SM2KeyExport;
import org.bcia.javachain.csp.gm.sm2.SM2SignerOpts;
import org.bcia.javachain.csp.gm.sm2.SM2KeyGenOpts;
import org.bcia.javachain.csp.gm.sm2.util.SM2KeyUtil;
import org.bcia.javachain.msp.IIdentity;
import org.bcia.javachain.msp.ISigningIdentity;
import org.bcia.javachain.msp.entity.GmSoftConf;
import org.bcia.javachain.msp.entity.OUIdentifier;
import org.bcia.javachain.protos.common.MspPrincipal;

import static org.bcia.javachain.csp.factory.CspManager.getDefaultCsp;

/**
 * @author zhangmingyang
 * @Date: 2018/3/28
 * @company Dingxuan
 */
public class GmSoftSigningIdentity implements ISigningIdentity {
    private static JavaChainLog log = JavaChainLogFactory.getLog(GmSoftSigningIdentity.class);
    private  GmSoftConf gmSoftConf;
    public GmSoftSigningIdentity(GmSoftConf gmSoftConf) {
      this.gmSoftConf=gmSoftConf;
    }

    @Override
    public byte[] sign(byte[] msg) {
        try {
            log.info("获取配置"+gmSoftConf.getMspId());
            SM2KeyExport sm2KeyExport= (SM2KeyExport) SM2KeyUtil.getKey(new SM2KeyGenOpts());
            byte[] signvalue= getDefaultCsp().sign(sm2KeyExport,msg,new SM2SignerOpts());
            log.info("signvalue is ok");
            return signvalue;
        } catch (JavaChainException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public IIdentity getPublicVersion() {
        return null;
    }

    @Override
    public String getMSPIdentifier() {
        return null;
    }

    @Override
    public void validate() {

    }

    @Override
    public OUIdentifier[] getOrganizationalUnits() {
        return new OUIdentifier[0];
    }

    @Override
    public void verify(byte[] msg, byte[] sig) {
        SM2KeyExport sm2KeyExport;
        try {
            sm2KeyExport = (SM2KeyExport) SM2KeyUtil.getKey(new SM2KeyGenOpts());

            boolean verify= getDefaultCsp().verify(sm2KeyExport,sig,msg,new SM2SignerOpts());
            log.info("验证结果："+verify);
        } catch (JavaChainException e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public void satisfiesPrincipal(MspPrincipal principal) {

    }
}
