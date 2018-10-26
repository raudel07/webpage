package sample.Constantes;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Configs {
    public static  final String UserName="";
    public static final String[] KEYWORDS = new String[] {
           "inicio","fin","negritaI","negritaF","parrafoI","parrafoF","tituloI","tituloF",
            "salto","menunavI","menunavF","ubic","encabezadoI","encabezadoF","piepagI","piepagF","articuloI","articuloF",
            "seccionI","seccionF","Espacioblanco"
    };

    public static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    public static final String PAREN_PATTERN = "\\(|\\)";
    public static final String BRACE_PATTERN = "\\{|\\}";
    public static final String BRACKET_PATTERN = "\\[|\\]";
    public static final String SEMICOLON_PATTERN = "\\;";
    public static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    public static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    public static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );

    public static final String sampleCode = String.join("\n", new String[] {
            "(inicio)",
            "",
            "(encabezadoI)",
            "",
            "(tituloI)\"Hola mundo\"(tituloF)",
            "",
            "(encabezadoF)",
            "",
            "(articuloI)",
            "",
            "(parrafoI)\"bienvenido a webpage espero que eches a bolar al maximo tu imaginacion\"(parrafoF)",
            "",
            "(parrafoI)\"realiza cualquier idea que se te venga a la mente\"(parrafoF)",
            "",
            "(articuloF)",
            "",
            "(fin)"
    });
    public static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    public static String[] EXPRECIONES={
            "[(]inicio[)]|([\\s]+|(?![\\s]))[(]inicio[)]//([\\w]+[ ]+)+|[(]fin[)]//[\\w]+",
            "[(]fin[)]|[(]fin[)]//([\\w]+[ ]+)+|[(]fin[)]//[\\w]+",
            "[(]encabezadoI[)]|[(]encabezadoI[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]encabezadoF[)]|[(]encabezadoF[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]tituloI[)][\"]([\\w]+[ ]+|[\\w]+)+[\"][(]tituloF[)]|[(]tituloI[)][\"]([\\w]+[ ]+|[\\w]+)+[\"][(]tituloF[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]piepagI[)]|[(]piepagI[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]piepagF[)]|[(]piepagF[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]articuloI[)]|[(]articuloI[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]articuloF[)]|[(]articuloF[)]//([\\w]+[ ]+)+|//[\\w]+",
            "[(]parrafoI[)][\"]([\\w]+[ ]+|[\\w]+)+[\"][(]parrafoF[)]|[(]parrafoI[)][\"]([\\w]+[ ]+|[\\w]+)+[\"][(]parrafoF[)]//([\\w]+[ ]+)+|//[\\w]+",
            "([\\s]+|(?![\\s]))//([\\s]+|(?![\\s]))([\\w]+[ ]+)+|([\\s]+|(?![\\s]))//([\\s]+|(?![\\s]))[\\w]+"
    };
}
