# Proiect PAO Popescu Tiberiu-Andrei grupa 252

# Etapa 1

Lista de tipuri de obiecte (entitati):

1. Pacient

2. Medic

3. Programare

4. Clinica

5. Orar disponibil / Calendar (pentru medici)

6. Camera

7. Fisa medicala

8. Boala

Lista de actiuni/interogari pentru aplicatia de programari medicale:

1. Programarea unei consultatii

2. Vizualizarea listei tuturor medicilor

3. Vizualizarea listei clinicilor disponibile

4. Modificarea unei programari deja existente

5. Anularea unei programari

6. Verificarea disponibilitatii unui medic pentru un anumit interval orar

7. Vizualizarea listei tuturor pacientilor care au vizitat un anumit medic

8. Obtinerea primului interval orar liber pentru un medic

9. Vizualizarea tuturor programarilor pentru un anumit medic

10. Vizualizarea tuturor programarilor pentru un anumit pacient

## 📋 Cerințe de business

Această aplicație răspunde unui set clar de cerințe specifice unei clinici sau unui cabinet medical:

- Gestionarea programărilor medicale:
  - Programare consultație pentru un pacient la un medic într-un anumit interval orar
  - Verificarea disponibilității medicului
  - Anularea sau reprogramarea unei consultații
  - Căutarea celui mai apropiat interval orar liber pentru un medic

- Administrarea entităților:
  - Gestiunea pacienților, medicilor și clinicilor
  - Istoric al consultațiilor pentru fiecare pacient
  - Vizualizarea tuturor pacienților care au consultat un anumit medic

- Informații medicale:
  - Asocierea diagnosticului și bolilor la consultații
  - Vizualizarea fișei medicale a unui pacient

## 🧠 Arhitectură

Aplicația este scrisă în Java, având o structură clară pe pachete:

- `src.Entities`: entitățile de bază — `Pacient`, `Medic`, `Clinica`, `Programare`, `Consultatie`
- `src.Services`: servicii pentru gestionarea logicii aplicației
- `src.Config`: clasa pentru configurarea bazei de date
- `src.Repositiories`: functionalitati pentru interactionarea cu baza de date
- `src.Constants`: constante globale, inclusiv calea către fișierul de audit
- `src.Utils`: utilitati, precum enum-uri
- `src.Main`: punctul de pornire al aplicației

Datele sunt persistate într-o bază de date relațională (MySQL), definită prin scriptul `init_databse.sql`.

## 💽 Persistență & Audit

Aplicația păstrează:
- toate acțiunile importante într-un fișier `audit.csv`, cu timestamp pentru fiecare eveniment
- datele utilizatorilor în tabele relaționale, legate logic între ele
