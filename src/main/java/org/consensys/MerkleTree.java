package org.consensys;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private MerkleNode root;

    public MerkleTree(List<byte[]> leaves) throws Exception {
        this.root = buildTree(leaves);
    }

    private MerkleNode buildTree(List<byte[]> leaves) throws Exception {
        List<MerkleNode> nodes = new ArrayList<>();
        for (byte[] leaf : leaves) {
            nodes.add(new MerkleNode(HashUtil.hash(leaf))); // Yaprak düğümleri oluştur
        }

        while (nodes.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                MerkleNode right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : nodes.get(i);
                nextLevel.add(new MerkleNode(nodes.get(i), right));
            }
            nodes = nextLevel;
        }
        return this.root = nodes.get(0); // Kök düğüm
    }

    public byte[] getRootHash() {
        return root.getHash();
    }

    // Insert a new leaf and rebuild the tree
    public void insertLeaf(byte[] data) throws Exception {
        List<byte[]> leaves = collectLeaves(root);
        leaves.add(data);
        this.root = buildTree(leaves);
    }

    // Collect all leaves in order
    private List<byte[]> collectLeaves(MerkleNode node) {
        List<byte[]> leaves = new ArrayList<>();
        if (node.getLeft() == null && node.getRight() == null) {
            leaves.add(node.getHash());
        } else {
            if (node.getLeft() != null) leaves.addAll(collectLeaves(node.getLeft()));
            if (node.getRight() != null) leaves.addAll(collectLeaves(node.getRight()));
        }
        return leaves;
    }

    public MerkleProof generateProof(int index, List<byte[]> leaves){
        List<byte[]> proof = new ArrayList<>();
        List<Boolean> proofDirections = new ArrayList<>(); // Proof yönlerini saklamak için
        List<MerkleNode> currentLevel = new ArrayList<>();

        // Yaprakları hash'leyerek Merkle düğümlerini oluştur
        for (byte[] leaf : leaves) {
            currentLevel.add(new MerkleNode(HashUtil.hash(leaf)));
        }

        while (currentLevel.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();

            for (int i = 0; i < currentLevel.size(); i += 2) {
                MerkleNode leftNode = currentLevel.get(i);
                MerkleNode rightNode = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : leftNode;

                // Eğer mevcut index bu seviyede birleştiriliyorsa, proof ekle
                if (index / 2 == i / 2) {
                    if (index % 2 == 0 && rightNode != null) {
                        proof.add(rightNode.getHash());
                        proofDirections.add(false); // Sağda
                    } else {
                        assert leftNode != null;
                        proof.add(leftNode.getHash());
                        proofDirections.add(true); // Solda
                    }
                }

                // Yeni seviyedeki düğümü oluştur
                assert rightNode != null;
                nextLevel.add(new MerkleNode(leftNode, rightNode));
            }

            currentLevel = nextLevel;
            index = index / 2; // Bir üst seviyeye geç
        }

        return new MerkleProof(proof, proofDirections);
    }
}
