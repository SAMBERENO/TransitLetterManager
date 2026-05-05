package com.example.listmanagmentapp.dto;

import java.util.Map;

public record CategoryDamage(
        int A, int B, int C, int D, int E, int F, int G, int H, int I,
        int J, int K, int L, int M, int N, int O, int P, int R,
        int S, int T, int U, int V, int W, int X
) {
    public Map<Character, CodesWithValues> getMap() {
        return Map.ofEntries(
                Map.entry('A', new CodesWithValues('A', A)),
                Map.entry('B', new CodesWithValues('B', B)),
                Map.entry('C', new CodesWithValues('C', C)),
                Map.entry('D', new CodesWithValues('D', D)),
                Map.entry('E', new CodesWithValues('E', E)),
                Map.entry('F', new CodesWithValues('F', F)),
                Map.entry('G', new CodesWithValues('G', G)),
                Map.entry('H', new CodesWithValues('H', H)),
                Map.entry('I', new CodesWithValues('I', I)),
                Map.entry('J', new CodesWithValues('J', J)),
                Map.entry('K', new CodesWithValues('K', K)),
                Map.entry('L', new CodesWithValues('L', L)),
                Map.entry('M', new CodesWithValues('M', M)),
                Map.entry('N', new CodesWithValues('N', N)),
                Map.entry('O', new CodesWithValues('O', O)),
                Map.entry('P', new CodesWithValues('P', P)),
                Map.entry('R', new CodesWithValues('R', R)),
                Map.entry('S', new CodesWithValues('S', S)),
                Map.entry('T', new CodesWithValues('T', T)),
                Map.entry('U', new CodesWithValues('U', U)),
                Map.entry('V', new CodesWithValues('V', V)),
                Map.entry('W', new CodesWithValues('W', W)),
                Map.entry('X', new CodesWithValues('X', X))
        );
    }
}
