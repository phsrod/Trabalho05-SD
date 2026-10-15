package br.edu.ufpi.sd.sentiment;

import smile.classification.KNN;

import java.util.Locale;
import java.util.Set;

public final class SentimentClassifier {
    private static final String[] LABELS = {"NEGATIVA", "NEUTRA", "POSITIVA"};
    private static final Set<String> FEATURES = Set.of(
            "excelente", "ótimo", "otimo", "superou", "rápido", "rapido", "qualidade",
            "funciona", "razoável", "razoavel", "especial", "danificada", "defeito",
            "decepcionado", "parou", "ruim");
    private final KNN<double[]> model;

    public SentimentClassifier() {
        double[][] training = {
                vector("excelente qualidade produto rápido"),
                vector("ótimo superou expectativas"),
                vector("produto funciona razoável"),
                vector("não apresenta nada especial"),
                vector("embalagem danificada produto defeito"),
                vector("embalagem veio danificada produto parou funcionar"),
                vector("decepcionado produto parou defeito")};
        int[] labels = {2, 2, 1, 1, 0, 0, 0};
        model = KNN.fit(training, labels, 1);
    }

    public String classify(String text) {
        return LABELS[model.predict(vector(text))];
    }

    private static double[] vector(String text) {
        String normalized = text.toLowerCase(Locale.ROOT);
        double[] values = new double[FEATURES.size()];
        int index = 0;
        for (String feature : FEATURES) {
            values[index++] = normalized.contains(feature) ? 1 : 0;
        }
        return values;
    }
}
