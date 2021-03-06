package org.bcia.julongchain.csp.gm.ftsafe;

import org.bcia.julongchain.common.exception.JulongChainException;
import org.bcia.julongchain.csp.factory.ICspFactory;
import org.bcia.julongchain.csp.factory.IFactoryOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.GMT0016CspFactory;
import org.bcia.julongchain.csp.gmt0016.ftsafe.GMT0016FactoryOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.IGMT0016Csp;
import org.bcia.julongchain.csp.gmt0016.ftsafe.ec.ECCOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.entity.GMT0016Lib;
import org.bcia.julongchain.csp.gmt0016.ftsafe.rsa.RSAOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.util.GMHashOpts;
import org.bcia.julongchain.csp.gmt0016.ftsafe.util.GMSignOpts;
import org.bcia.julongchain.csp.intfs.ICsp;
import org.bcia.julongchain.csp.intfs.IKey;
import org.bcia.julongchain.csp.intfs.opts.IHashOpts;
import org.bcia.julongchain.csp.intfs.opts.IKeyGenOpts;
import org.bcia.julongchain.csp.intfs.opts.ISignerOpts;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
/**
 * Test methods to sign component GMT0016
 *
 * @author zhaoxiaobo
 * @date 2018/08/26
 * @company FEITIAN
 */
public class GMT0016CpsSignTest {
	private ICsp csp;

    @Before
    public void before() throws JulongChainException{
        System.out.println("==========class GMT0016Cps function sign test start==========");

        GMT0016Lib gmt0016Lib = new GMT0016Lib("/home/bcia/libes_3000gm.so","ePass3000GM","0955381103160217","rockey","123456","ENTERSAFE-ESPK");
        IFactoryOpts factoryOpts = new GMT0016FactoryOpts(gmt0016Lib);
        Assert.assertNotNull(factoryOpts);
        ICspFactory cspFactory = new GMT0016CspFactory();
        Assert.assertNotNull(cspFactory);
        csp = cspFactory.getCsp(factoryOpts);
        Assert.assertNotNull(csp);
    }

    @Test
    public void testSignBySHA1() throws JulongChainException{
        System.out.println("==========test function sign and IHashOpts instance GMHashOpts.SHA1HashOpts==========");
        //1:get IKey
        IKeyGenOpts rsa1024keyGenOpts = new RSAOpts.RSA1024KeyGenOpts(false);
        IKey rsa1024Key = csp.keyGen(rsa1024keyGenOpts);
        Assert.assertNotNull(rsa1024Key);
        //2:get IHashOpts
        String message = "JulongChain";
        IHashOpts sha1HashOpts = new GMHashOpts.SHA1HashOpts();
        byte[] sha1hash = csp.hash(message.getBytes(), sha1HashOpts);
        Assert.assertNotNull(sha1hash);
        //3:execute sign
        ISignerOpts opts = GMSignOpts.SHA1;
        byte[] sign = csp.sign(rsa1024Key, sha1hash, opts);
        Assert.assertNotNull(sign);
    }

    @Test
    public void testSignBySHA256() throws JulongChainException{
        System.out.println("==========test function sign and IHashOpts instance GMHashOpts.SHA256HashOpts==========");
        //1:get IKey
        IKeyGenOpts rsa1024keyGenOpts = new RSAOpts.RSA1024KeyGenOpts(false);
        IKey rsa1024Key = csp.keyGen(rsa1024keyGenOpts);
        Assert.assertNotNull(rsa1024Key);
        //2:get IHashOpts
        String message = "JulongChain";
        IHashOpts sha256HashOpts = new GMHashOpts.SHA256HashOpts();
        byte[] sha256hash = csp.hash(message.getBytes(), sha256HashOpts);
        Assert.assertNotNull(sha256hash);
        //3:execute sign
        ISignerOpts opts = GMSignOpts.SHA256;
        byte[] sign = csp.sign(rsa1024Key, sha256hash, opts);
        Assert.assertNotNull(sign);
    }

    @Test
    public void testSignBySM3() throws JulongChainException{
        System.out.println("==========test function sign and IHashOpts instance GMHashOpts.SM3HashOpts==========");
        //1:get IKey
        IKeyGenOpts eccKeyGenOpts = new ECCOpts.ECCKeyGenOpts(false);
        IKey eccKey = csp.keyGen(eccKeyGenOpts);
        Assert.assertNotNull(eccKey);
        //2:get IHashOpts
        String message = "JulongChain";
        IHashOpts sm3HashOpts = new GMHashOpts.SM3HashOpts();
        byte[] sm3Hash = csp.hash(message.getBytes(), sm3HashOpts);
        Assert.assertNotNull(sm3Hash);
        //3:execute sign
        ISignerOpts opts = GMSignOpts.SM3;
        byte[] sign = csp.sign(eccKey, sm3Hash, opts);
        Assert.assertNotNull(sign);
    }


    @After
    public void after()  throws JulongChainException{
        System.out.println("==========test end==========");
        ((IGMT0016Csp)csp).finalized();
    }
}
