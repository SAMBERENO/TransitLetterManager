package com.example.listmanagmentapp.dto;

import java.util.List;

public record CategoryDamage(
        int A, int B, int C, int D, int E, int F, int G, int H, int I,
        int J, int K, int L, int M, int N, int O, int P, int R,
        int S, int T, int U, int V, int W, int X
) {
    public List<Integer> getValues() {
        return List.of(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, R, S, T, U, V, W, X);
    }

}
