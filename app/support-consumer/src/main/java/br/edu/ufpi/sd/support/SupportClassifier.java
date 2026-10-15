package br.edu.ufpi.sd.support;

import smile.classification.KNN;

import java.util.Locale;
import java.util.Set;

public final class SupportClassifier {
    private static final String[] LABELS = {"PROBLEMA DE ACESSO", "PROBLEMA DE DESEMPENHO",
            "PROBLEMA DE CONEXÃO"};
    private static final Set<String> FEATURES = Set.of(
            "acessar", "conta", "senha", "login", "bloqueado", "lento", "demora",
            "carregar", "relatórios", "relatorios", "conexão", "conexao", "cai",
            "servidor", "enviar");
    private final KNN<double[]> model;

    public SupportClassifier() {
        double[][] training = {
                vector("não consigo acessar conta senha login bloqueado"),
                vector("senha recusada login bloqueado"),
                vector("aplicativo muito lento demora carregar relatórios"),
                vector("sistema demora minutos carregar"),
                vector("conexão cai enviar arquivo"),
                vector("serviço perdeu conexão servidor")};
        int[] labels = {0, 0, 1, 1, 2, 2};
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
