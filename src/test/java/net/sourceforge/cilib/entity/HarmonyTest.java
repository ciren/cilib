package net.sourceforge.cilib.entity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HarmonyTest {

	@Test
	public void equals() {
		Harmony h1 = new Harmony();
		Harmony h2 = new Harmony();
		
		assertTrue(h1.equals(h2));
		assertTrue(h2.equals(h1));
		assertTrue(h1.equals(h1));
		
		assertFalse(h1.equals(null));
	}
	
	@Test
	public void hashCodes() {
		Harmony h1 = new Harmony();
		Harmony h2 = new Harmony();
		
		assertTrue(h1.hashCode() == h2.hashCode());
	}
}
