package org.consensys;

import java.util.Arrays;
import java.util.List;

public class MerkleProof {
    private List<byte[]> proof;
    private List<Boolean> proofDirections;

    public MerkleProof(List<byte[]> proof,  List<Boolean> proofDirections) {
        this.proof = proof;
        this.proofDirections = proofDirections;
    }

    public List<Boolean> getProofDirections()
    {
        return proofDirections;
    }

    public boolean verify(byte[] leaf, byte[] expectedRoot, List<Boolean> proofDirections) throws Exception {
        byte[] currentHash = HashUtil.hash(leaf);

        for (int i = 0; i < proof.size(); i++) {
            byte[] proofHash = proof.get(i);
            boolean isLeft = proofDirections.get(i); // Sol mu sağ mı?
            currentHash  =!isLeft ? HashUtil.hash(currentHash, proofHash): HashUtil.hash(proofHash, currentHash);
        }
        return Arrays.equals(currentHash, expectedRoot);
    }
}
