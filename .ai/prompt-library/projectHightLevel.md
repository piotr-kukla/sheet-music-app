# Aplikacja - SheetMusicParser (MVP)

### Główny problem
Parsowanie plików PDF zawierających zapis nutowy utworów chóralnych. Generowanie plików muzycznych dla całego chóru. 
Aplikacja próbuje parsować każdą pięciolinię osobno. Jeśli więc poszczególne głosy (np. sopran, alt, tenor bas) mają osobne pięciolnie 
wtedy dla każdego głosu otrzymamy osobny plik MIDI.


### Najmniejszy zestaw funkcjonalności
- Zalogowany użytkownik ma możliwość wysłania pliku PDF na serwer za zapisem nutowym utworu muzycznego.
- Serwer parsuje plik PDF i zapisuje pliki muzyczne.
- Aplikacja prezentuje wszyskie dotychczas zapisane utwory bez potrzeby logowania
- Prosty system kont użytkowników do powiązania użytkownika z własnymi utworami


### Co NIE wchodzi w zakres MVP
- Przesłuchiwane pliki nie mają podglądu na nuty podczas odtwarzania.
- Nie robimy żadnej korekty wyników parsowania. Zakładamy że parser ma 100% poprawność.

