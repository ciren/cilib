/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Utility methods used in the I/O packages.
 */
public final class IOUtils {

    public enum Block {
        KILOBYTE(1024),
        MEGABYTE(1024*1024);

        private final int bytes;

        Block(int size) {
            this.bytes = size;
        }

        public int size() {
            return this.bytes;
        }
    }

    private static int characterBufferSize = 4 * Block.KILOBYTE.size();

    private IOUtils() {}

    /**
     * Writes a variable number of string to the given ByteBuffer.
     * @param buffer the ByteBuffer to put data in.
     * @param strings the string to write to the buffer.
     * @throws java.nio.charset.CharacterCodingException An encoding exception
     * that could occur during the writing of the strings.
     */
    public static void writeStrings(ByteBuffer buffer, String... strings) throws CharacterCodingException {
        for (String string : strings) {
            CharBuffer cb = CharBuffer.allocate(characterBufferSize);
            cb.put(string);
            cb.flip();
            Charset cs = Charset.defaultCharset();
            CharsetEncoder cse = cs.newEncoder();
            buffer.put(cse.encode(cb));
        }
    }

    /**
     * Writes the given string to the given ByteBuffer.
     * @param buffer the buffer to write to.
     * @param string the string to write the buffer to.
     * @throws java.nio.charset.CharacterCodingException An encoding exception
     * that could occur during the writing of the strings.
     */
    public static void writeString(ByteBuffer buffer, String string) throws CharacterCodingException {
        CharBuffer cb = CharBuffer.allocate(characterBufferSize);
        cb.put(string);
        cb.flip();
        Charset cs = Charset.defaultCharset();
        CharsetEncoder cse = cs.newEncoder();
        buffer.put(cse.encode(cb));
    }

    /**
     * Writes the given string as a new line to the ByteBuffer.
     * @param buffer the buffer to write to.
     * @param string the line to write.
     * @throws java.nio.charset.CharacterCodingException An encoding exception
     * that could occur during the writing of the strings.
     */
    public static void writeLine(ByteBuffer buffer, String string) throws CharacterCodingException {
        CharBuffer cb = CharBuffer.allocate(characterBufferSize);
        cb.put(string);
        cb.put('\n');
        cb.flip();
        Charset cs = Charset.defaultCharset();
        CharsetEncoder cse = cs.newEncoder();
        buffer.put(cse.encode(cb));
    }

    /**
     * Gets the buffer size of the temporary buffer used during writing.
     * @return the character buffer size.
     */
    public static int getCharacterBufferSize() {
        return characterBufferSize;
    }

    /**
     * Sets the buffer size of the temporary buffer used during writing.
     * @param characterBufferSize the new character buffer size.
     */
    public static void setCharacterBufferSize(int characterBufferSize) {
        IOUtils.characterBufferSize = characterBufferSize;
    }

}
