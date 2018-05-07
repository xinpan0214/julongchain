/**
 * Copyright BCIA. All Rights Reserved.
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
package org.bcia.javachain.common.tools.cryptogen;

import org.bcia.javachain.common.exception.JavaChainException;
import org.bcia.javachain.common.tools.cryptogen.bean.KeySinger;
import org.bcia.javachain.common.tools.cryptogen.bean.MockKey;
import org.bcia.javachain.csp.intfs.IKey;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;

import static org.junit.Assert.*;


public class CspHelperTest {


    String testDir;

    {
        try {
            Path tempDirPath = Files.createTempDirectory(null);
            testDir = Paths.get(tempDirPath.toString(), "csp-test").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadPrivateKey() throws JavaChainException {

        IKey priv = CspHelper.generatePrivateKey(testDir);
        String filePath = Paths.get(testDir, Hex.toHexString(priv.ski()) + "_sk").toString();
        File pkFile = new File(filePath);
        assertTrue(pkFile.exists());

        IKey loadedPriv = CspHelper.loadPrivateKey(filePath);
        // TODO wait GmCsp.importKey
//        assertNotNull(loadedPriv);
//        assertEquals(priv.ski(), loadedPriv.ski());

        FileUtil.removeAll(testDir);
    }

    @Test
    public void generatePrivateKey() throws JavaChainException {
        System.out.println(testDir);
        IKey priv = CspHelper.generatePrivateKey(testDir);
        KeySinger keySinger = new KeySinger(priv, priv.getPublicKey());

        assertNotNull(priv);
        assertTrue(priv.isPrivate());
        assertNotNull(keySinger);
        File pkFile = new File(Paths.get(testDir, Hex.toHexString(priv.ski()) + "_sk").toUri());
        assertTrue(pkFile.exists());

        FileUtil.removeAll(testDir);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getECPublicKey() throws JavaChainException {

        IKey priv = CspHelper.generatePrivateKey(testDir);
        ECPublicKey ecPubKey = CspHelper.getSM2PublicKey(priv);

        assertTrue(ecPubKey instanceof PublicKey);

        //force errors using mockKey
        expectedException.expect(JavaChainException.class);
        priv = new MockKey(null, null, new MockKey());
        CspHelper.getSM2PublicKey(priv);

        priv = new MockKey(null, null, new MockKey(null, "bytesErr", null));

        expectedException.expect(JavaChainException.class);
        expectedException.expectMessage(((MockKey) priv).getBytesErr());
        CspHelper.getSM2PublicKey(priv);

        priv = new MockKey("pubKeyErr", null, new MockKey());

        expectedException.expectMessage(((MockKey) priv).getPubKeyErr());
        expectedException.expect(JavaChainException.class);
        CspHelper.getSM2PublicKey(priv);

        FileUtil.removeAll(testDir);
    }
}