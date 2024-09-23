package org.consensys;

public class MerkleNode {
    private final byte[] hash;
    private MerkleNode left;
    private MerkleNode right;

    // Constructor for leaf node
    public MerkleNode(byte[] hash) {
        this.hash = hash;
    }

    // Constructor for internal node
    public MerkleNode(MerkleNode left, MerkleNode right){
        this.left = left;
        this.right = right;
        this.hash = HashUtil.hash(left.getHash(), right.getHash());
    }

    public byte[] getHash() {
        return hash;
    }

    public MerkleNode getLeft() {
        return left;
    }

    public MerkleNode getRight() {
        return right;
    }
}
