/*
 *  Copyright (C) 2006  Shawn Pearce <spearce@spearce.org>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public
 *  License, version 2, as published by the Free Software Foundation.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 */
package org.spearce.jgit.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import junit.framework.TestCase;

public abstract class RepositoryTestCase extends TestCase {

	protected final File trashParent = new File("trash");

	protected File trash;

	protected File trash_git;

	protected static final PersonIdent jauthor;

	protected static final PersonIdent jcommitter;

	static {
		jauthor = new PersonIdent("J. Author", "jauthor@example.com");
		jcommitter = new PersonIdent("J. Committer", "jcommitter@example.com");
	}

	protected static void recursiveDelete(final File dir) {
		final File[] ls = dir.listFiles();
		if (ls != null) {
			for (int k = 0; k < ls.length; k++) {
				final File e = ls[k];
				if (e.isDirectory()) {
					recursiveDelete(e);
				} else {
					e.delete();
				}
			}
		}
		dir.delete();
		if (dir.exists()) {
			System.out.println("Warning: Failed to delete " + dir);
		}
	}

	protected static void copyFile(final File src, final File dst)
			throws IOException {
		final FileInputStream fis = new FileInputStream(src);
		final FileOutputStream fos = new FileOutputStream(dst);
		final byte[] buf = new byte[4096];
		int r;
		while ((r = fis.read(buf)) > 0) {
			fos.write(buf, 0, r);
		}
		fis.close();
		fos.close();
	}

	protected File writeTrashFile(final String name, final String data)
			throws IOException {
		File tf = new File(trash, name);
		File tfp = tf.getParentFile();
		if (!tfp.exists() && !tf.getParentFile().mkdirs())
			throw new Error("Could not create directory " + tf.getParentFile());
		final OutputStreamWriter fw = new OutputStreamWriter(
				new FileOutputStream(tf), "UTF-8");
		fw.write(data);
		fw.close();
		return tf;
	}

	protected static void checkFile(File f, final String checkData)
			throws IOException {
		Reader r = new InputStreamReader(new FileInputStream(f), "ISO-8859-1");
		char[] data = new char[(int) f.length()];
		if (f.length() !=  r.read(data))
			throw new IOException("Internal error reading file data from "+f);
		assertEquals(checkData, new String(data));
	}

	protected Repository db;

	public void setUp() throws Exception {
		super.setUp();
		recursiveDelete(trashParent);
		trash = new File(trashParent,"trash"+System.currentTimeMillis());
		trash_git = new File(trash, ".git");

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				recursiveDelete(trashParent);
			}
		});

		db = new Repository(trash_git);
		db.create();

		final String[] packs = {
				"pack-34be9032ac282b11fa9babdc2b2a93ca996c9c2f",
				"pack-df2982f284bbabb6bdb59ee3fcc6eb0983e20371",
				"pack-9fb5b411fe6dfa89cc2e6b89d2bd8e5de02b5745"
		};
		final File tst = new File("tst");
		final File packDir = new File(db.getObjectsDirectory(), "pack");
		for (int k = 0; k < packs.length; k++) {
			copyFile(new File(tst, packs[k] + ".pack"), new File(packDir,
					packs[k] + ".pack"));
			copyFile(new File(tst, packs[k] + ".idx"), new File(packDir,
					packs[k] + ".idx"));
		}

		copyFile(new File(tst, "packed-refs"), new File(trash_git,"packed-refs"));

		db.scanForPacks();
	}

	protected void tearDown() throws Exception {
		db.close();
		super.tearDown();
	}

	/**
	 * Helper for creating extra empty repos
	 *
	 * @return a new empty git repository for testing purposes
	 *
	 * @throws IOException
	 */
	protected Repository createNewEmptyRepo() throws IOException {
		File newTestRepo = new File(trashParent, "new"+System.currentTimeMillis()+"/.git");
		assertFalse(newTestRepo.exists());
		File unusedDir = new File(trashParent, "tmp"+System.currentTimeMillis());
		assertTrue(unusedDir.mkdirs());
		final Repository newRepo = new Repository(newTestRepo);
		newRepo.create();
		return newRepo;
	}

}
