package org.bcia.julongchain.common.ledger.util.leveldbhelper;

import org.bcia.julongchain.common.ledger.util.IDBHandler;
import org.bcia.julongchain.common.ledger.util.IDBProvider;
import org.bcia.julongchain.core.ledger.ledgerconfig.LedgerConfig;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.bcia.julongchain.common.ledger.util.Utils.*;

/**
 *
 * 使用
 * http://192.168.1.165:3000/
 * 查看leveldb
 *
 * @author sunzongyu
 * @date 2018/08/20
 * @company Dingxuan
 */
public class LevelDBProviderTest {
	static LevelDBProvider provider;
	static String workSpace = "/tmp/julongchain/levledb";
	static String dir;
	static String groupID = "myGroup";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void beforeClass() throws Exception {
		rmrf(workSpace);
	}

	@Before
	public void setUp() throws Exception {
		dir = workSpace + File.separator + UUID.randomUUID();
		provider = new LevelDBProvider(dir);
		provider.put("a".getBytes(), "a".getBytes(), true);
		provider.setLedgerID(groupID);
		provider.put("b".getBytes(), "b".getBytes(), true);
		provider.setLedgerID(null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getDBHandle() throws Exception {
		IDBProvider db = provider.getDBHandle("myGroup");
		assertNotNull(db);
		assertEquals(db, provider.getDBHandle("myGroup"));
	}

	@Test
	public void getDBPath() {
		String dbPath = provider.getDBPath();
		assertEquals(dir, dbPath);
	}

	@Test
	public void get() throws Exception{
		byte[] a;
		byte[] b;
		a = provider.get("a".getBytes());
		b = provider.get("b".getBytes());
		assertNotNull(a);
		assertArrayEquals("a".getBytes(), a);
		assertNull(b);

		provider.setLedgerID(groupID);
		a = provider.get("a".getBytes());
		b = provider.get("b".getBytes());
		assertNull(a);
		assertNotNull(b);
		assertArrayEquals("b".getBytes(), b);

		//不存在
		assertNull(provider.get("x".getBytes()));

		//查询空
		assertNull(provider.get(null));
	}

	@Test
	public void put() throws Exception {
		byte[] c;
		byte[] d;
		c = provider.get("c".getBytes());
		assertNull(c);
		provider.put("c".getBytes(), "c".getBytes(), true);
		c = provider.get("c".getBytes());
		assertNotNull(c);
		assertArrayEquals("c".getBytes(), c);

		//k,v不能为空
		thrown.expect(Exception.class);
		provider.put(null, null, true);
		provider.put(null, "x".getBytes(), true);
		provider.put("x".getBytes(), null, true);

		provider.setLedgerID(groupID);
		d = provider.get("d".getBytes());
		assertNull(d);
		provider.put("d".getBytes(), "d".getBytes(), true);
		d = provider.get("d".getBytes());
		assertNotNull(d);
		assertArrayEquals("d".getBytes(), d);

		//v不能为空
		thrown.expect(Exception.class);
		provider.put(null, null, true);
		provider.put("x".getBytes(), null, true);
	}

	@Test
	public void delete() throws Exception {
		byte[] a;
		byte[] b;
		a = provider.get("a".getBytes());
		assertNotNull(a);
		assertArrayEquals("a".getBytes(), a);
		provider.delete("a".getBytes(), true);
		a = provider.get("a".getBytes());
		assertNull(a);

		//空
		thrown.expect(Exception.class);
		provider.delete(null, true);

		provider.setLedgerID(groupID);
		b = provider.get("b".getBytes());
		assertNotNull(b);
		assertArrayEquals("b".getBytes(), b);
		provider.delete("b".getBytes(), true);
		b = provider.get("b".getBytes());
		assertNull(b);

		//空
		provider.delete(null, true);
	}

	@Test
	public void writeBatch() throws Exception {
		byte[] a;
		byte[] b;
		byte[] c;
		byte[] d;
		UpdateBatch updateBatch;

		a = provider.get("a".getBytes());
		c = provider.get("c".getBytes());
		assertNotNull(a);
		assertArrayEquals("a".getBytes(), a);
		assertNull(c);
		updateBatch = new UpdateBatch();
		updateBatch.delete("a".getBytes());
		updateBatch.put("c".getBytes(), "c".getBytes());
		provider.writeBatch(updateBatch, true);
		a = provider.get("a".getBytes());
		c = provider.get("c".getBytes());
		assertNull(a);
		assertNotNull(c);
		assertArrayEquals("c".getBytes(), c);

		//k,v不能为空
		thrown.expect(Exception.class);
		updateBatch = new UpdateBatch();
		updateBatch.delete(null);
		updateBatch.put(null, null);
		updateBatch.put("x".getBytes(), null);
		updateBatch.put(null, "x".getBytes());
		provider.writeBatch(updateBatch, true);

		provider.setLedgerID(groupID);
		b = provider.get("b".getBytes());
		d = provider.get("d".getBytes());
		assertNotNull(b);
		assertArrayEquals("b".getBytes(), b);
		assertNull(d);
		updateBatch = new UpdateBatch();
		updateBatch.delete("b".getBytes());
		updateBatch.put("d".getBytes(), "d".getBytes());
		provider.writeBatch(updateBatch, true);
		b = provider.get("b".getBytes());
		d = provider.get("d".getBytes());
		assertNull(b);
		assertNotNull(d);
		assertArrayEquals("d".getBytes(), d);

		//空
		thrown.expect(Exception.class);
		updateBatch = new UpdateBatch();
		updateBatch.delete(null);
		updateBatch.put(null, null);
		updateBatch.put("x".getBytes(), null);
		updateBatch.put(null, "x".getBytes());
		provider.writeBatch(updateBatch, true);
	}

	@Test
	public void getIterator() throws Exception {
		Iterator<Map.Entry<byte[], byte[]>> iterator;
		int i;
		iterator = provider.getIterator(null);
		assertNotNull(iterator);
		i = 0;
		while (iterator.hasNext()) {
			i++;
			Map.Entry<byte[], byte[]> next = iterator.next();
			assertNotNull(next.getKey());
			assertNotNull(next.getValue());
		}
		assertSame(2, i);

		iterator = provider.getIterator("m".getBytes());
		assertNotNull(iterator);
		i = 0;
		while (iterator.hasNext()) {
			i++;
			Map.Entry<byte[], byte[]> next = iterator.next();
			assertNotNull(next.getKey());
			assertNotNull(next.getValue());
		}
		assertSame(1, i);
	}
}