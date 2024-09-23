package org.consensys;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MerkleTreeTest
{

    @Test
    public void testMerkleTreeConstruction() throws Exception
    {
        List<byte[]> leaves = Arrays.asList("a".getBytes(), "b".getBytes(), "c".getBytes(), "d".getBytes());
        MerkleTree tree = new MerkleTree(leaves);
        assertNotNull(tree.getRootHash());
    }


    @Test
    public void testMerkleProof() throws Exception
    {
        List<byte[]> leaves = Arrays.asList("a".getBytes(), "b".getBytes(), "c".getBytes(), "d".getBytes());
        MerkleTree tree = new MerkleTree(leaves);

        for (int i = 0; i < leaves.size(); i++)
        {
            MerkleProof proof = tree.generateProof(i, leaves);
            assertTrue(proof.verify(leaves.get(i), tree.getRootHash(), proof.getProofDirections()));
        }
    }

    @Test
    public void testInsertLeaf() throws Exception
    {
        List<byte[]> leaves = Arrays.asList("a".getBytes(), "b".getBytes());
        MerkleTree tree = new MerkleTree(leaves);

        byte[] initialRoot = tree.getRootHash();
        tree.insertLeaf("c".getBytes());
        byte[] newRoot = tree.getRootHash();

        assertFalse(Arrays.equals(initialRoot, newRoot));
    }
}
