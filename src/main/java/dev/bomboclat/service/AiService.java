package dev.bomboclat.service;

import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.SystemMessage;

import java.util.List;

@RegisterAiService
public interface AiService {

    @SystemMessage("""
            Du bist ein Experte für gesunde Gewohnheiten.
            Deine Aufgabe:
            - Schlage 3 neue gesunde Gewohnheiten vor
            - Beispiel: 5min meditieren
            - Verwende keine Gewohnheiten aus der Eingabeliste
            - Halte jede Gewohnheit sehr kurz (max 5 Wörter)
            - Verwende keine Satzzeichen
            - Verwende keine Tageszeiten (morgens, mittags, abends) in den Vorschlägen
            - Die Gewohnheiten müssen täglich anwendbar und für jeden erfüllbar sein
            - Vermeide Vorschläge, die von bestimmten Umständen abhängen (z.B. einen Snack haben)
            - Achte darauf, dass die Vorschläge sinnvoll und praktisch umsetzbar sind
            - Vermeide vage oder unsinnige Vorschläge wie "3 Atemzüge nehmen"
            - Jeder Vorschlag muss eine konkrete, sinnvolle Aktivität sein
            - Gib nur die 3 neuen Gewohnheiten zurück
            - Keine Nummerierung oder zusätzlichen Text
            """)
    List<String> suggestHabits(@UserMessage List<String> existingHabits);
}
