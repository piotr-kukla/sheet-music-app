# Product Requirements Document (PRD)
## SheetMusicParser - MVP

**Wersja dokumentu:** 1.0  
**Data utworzenia:** 26 października 2025  
**Autor:** Product Manager  
**Status:** Draft → Ready for Development

---

## Table of Contents
1. [Executive Summary](#1-executive-summary)
2. [Problem Statement](#2-problem-statement)
3. [Goals & Success Metrics](#3-goals--success-metrics)
4. [User Personas](#4-user-personas)
5. [User Stories](#5-user-stories)
6. [Functional Requirements](#6-functional-requirements)
7. [Technical Architecture](#7-technical-architecture)
8. [Data Model](#8-data-model)
9. [API Specification](#9-api-specification)
10. [User Flows](#10-user-flows)
11. [UI/UX Guidelines](#11-uiux-guidelines)
12. [Out of Scope (MVP)](#12-out-of-scope-mvp)
13. [Development Phases](#13-development-phases)
14. [Risks & Mitigations](#14-risks--mitigations)
15. [Appendices](#15-appendices)

---

## 1. Executive Summary

**SheetMusicParser** to aplikacja webowa umożliwiająca automatyczne parsowanie plików PDF z zapisem nutowym utworów chóralnych i konwersję ich do plików MIDI. Aplikacja jest projektem edukacyjnym, którego głównym celem jest eksploracja możliwości wykorzystania LLM (Claude) w procesie developmentu.

### Kluczowe funkcjonalności MVP:
- System autoryzacji użytkowników (email + hasło)
- Upload plików PDF z nutami (max 10MB)
- Asynchroniczne parsowanie PDF → MIDI przy użyciu Audiveris
- Generowanie osobnych plików MIDI dla każdej pięciolinii
- Panel użytkownika z listą własnych utworów
- Pobieranie plików MIDI i oryginalnych PDF

### Technologie:
- **Backend:** Scala 3, Tapir, PostgreSQL (bazując na Bootzooka template)
- **Frontend:** React, TypeScript, shadcn/ui
- **Parser:** Audiveris (CLI)
- **Infrastructure:** Docker, lokalny storage

---

## 2. Problem Statement

### 2.1 Główny problem
Członkowie chórów amatorskich często potrzebują plików audio poszczególnych głosów (sopran, alt, tenor, bas) do nauki swoich partii. Przekształcenie skanów lub PDF-ów z nutami na odtwarzalne pliki MIDI jest procesem czasochłonnym i wymaga specjalistycznego oprogramowania.

### 2.2 Rozwiązanie
SheetMusicParser automatyzuje proces parsowania PDF-ów z zapisem nutowym, generując:
- Plik MIDI całego utworu
- Osobne pliki MIDI dla każdej pięciolinii (głosu)

Użytkownik uploaduje PDF, a aplikacja automatycznie rozpoznaje nuty i tworzy pliki do odtworzenia.

### 2.3 Kontekst projektu
Jest to **projekt edukacyjny** mający na celu:
- Naukę przez praktykę (hands-on z Scala, React, Docker)
- Eksplorację możliwości LLM (Claude) jako narzędzia wspomagającego development
- Brak celów komercyjnych, monetyzacji czy skalowania

---

## 3. Goals & Success Metrics

### 3.1 Cele biznesowe (edukacyjne)
1. **Cel główny:** Stworzenie działającego MVP aplikacji webowej z użyciem pomocy LLM
2. **Cel dodatkowy:** Zdobycie doświadczenia z technologiami: Scala 3, React, Audiveris, Docker

### 3.2 Cele produktowe
1. Umożliwienie użytkownikom łatwego uploadowania plików PDF z nutami
2. Automatyczne generowanie plików MIDI z rozbiciem na głosy
3. Prosty i intuicyjny interfejs użytkownika (desktop-only)

### 3.3 Kluczowe metryki sukcesu (KPIs)

| Metryka | Cel dla MVP | Sposób pomiaru |
|---------|-------------|----------------|
| Liczba pomyślnie przetworzonych plików | > 80% success rate | Logi backendowe, status w DB |
| Średni czas parsowania | < 2 minuty | Timestamp rozpoczęcia vs zakończenia |
| Liczba zarejestrowanych użytkowników (3 mies.) | 100 | PostgreSQL count |
| Współczynnik powrotu użytkowników | > 30% | Analytics / logi logowania |

### 3.4 Out of Metrics (nie mierzymy w MVP)
- Koszt infrastruktury (lokalne środowisko)
- Mobile traffic (desktop-only)
- Customer satisfaction score (brak ankiet w MVP)

---

## 4. User Personas

### Persona: Ania - członkini chóru amator

**Demografia:**
- Wiek: 35 lat
- Zawód: Księgowa
- Lokalizacja: Średnie miasto w Polsce
- Doświadczenie muzyczne: Chór amatorski przy parafii (3 lata)

**Technologia:**
- Komfortowo korzysta z aplikacji webowych
- Ma laptopa (Windows/Mac)
- Nie jest programistką ani muzykiem-profesjonalistą

**Potrzeby:**
- Potrzebuje plików audio swoich partii (alt) do nauki przed próbami
- Nie ma dostępu do specjalistycznego oprogramowania muzycznego
- Chce szybko i łatwo przekonwertować nuty z PDF na plik MIDI

**Frustracje:**
- Ręczne wprowadzanie nut do programów typu MuseScore jest czasochłonne
- Nie wie jak korzystać z zaawansowanych narzędzi jak Audiveris
- Często otrzymuje skany partytur w słabej jakości

**Cele:**
- Szybko uzyskać plik MIDI swojego głosu
- Móc odsłuchać partię przed próbami chóru
- Zarządzać swoimi utworami w jednym miejscu

---

## 5. User Stories

### 5.1 Rejestracja i logowanie

**US-01: Rejestracja użytkownika**
```
JAKO nowy użytkownik
CHCĘ zarejestrować się w aplikacji podając email i hasło
ABY móc uploadować swoje pliki PDF z nutami

Kryteria akceptacji:
- [ ] Formularz rejestracji z polami: email, hasło, powtórz hasło
- [ ] Walidacja: email musi być poprawny format, hasło min. 8 znaków
- [ ] Brak weryfikacji emaila (projekt edukacyjny)
- [ ] Po rejestracji użytkownik jest automatycznie zalogowany
- [ ] Błędy są wyświetlane w czytelny sposób (email już istnieje, hasła się różnią)
```

**US-02: Logowanie użytkownika**
```
JAKO zarejestrowany użytkownik
CHCĘ zalogować się do aplikacji
ABY uzyskać dostęp do swoich utworów

Kryteria akceptacji:
- [ ] Formularz logowania z polami: email, hasło
- [ ] Po poprawnym logowaniu użytkownik otrzymuje JWT token
- [ ] Token jest przechowywany w localStorage
- [ ] Przekierowanie do panelu użytkownika po zalogowaniu
- [ ] Komunikat o błędzie przy niepoprawnych danych
```

**US-03: Wylogowanie**
```
JAKO zalogowany użytkownik
CHCĘ móc się wylogować z aplikacji
ABY zabezpieczyć swoje konto

Kryteria akceptacji:
- [ ] Przycisk "Wyloguj" widoczny w nawigacji
- [ ] Po wylogowaniu token jest usuwany z localStorage
- [ ] Przekierowanie na stronę logowania
```

### 5.2 Upload i parsowanie

**US-04: Upload pliku PDF**
```
JAKO zalogowany użytkownik
CHCĘ uploadować plik PDF z nutami
ABY otrzymać pliki MIDI do odsłuchania

Kryteria akceptacji:
- [ ] Formularz z następującymi polami:
  - Upload pliku PDF (drag & drop lub file picker)
  - Tytuł utworu (pole tekstowe, wymagane)
  - Kompozytor (pole tekstowe, opcjonalne)
- [ ] Walidacja: tylko pliki .pdf, max 10MB
- [ ] Wyświetlenie nazwy wybranego pliku przed uploadem
- [ ] Progress bar przy wysyłaniu pliku na serwer
- [ ] Po uploadzie użytkownik widzi spinner "Parsowanie w toku..."
- [ ] Komunikat o błędzie jeśli plik jest za duży lub niepoprawny format
```

**US-05: Status parsowania**
```
JAKO użytkownik, który uploadował plik
CHCĘ widzieć status parsowania mojego pliku
ABY wiedzieć kiedy pliki MIDI będą gotowe

Kryteria akceptacji:
- [ ] Spinner z tekstem "Parsowanie w toku..." podczas parsowania
- [ ] Automatyczne odświeżanie statusu (polling co 5 sekund)
- [ ] Po zakończeniu jeden z trzech stanów:
  - SUCCESS: "Parsowanie zakończone pomyślnie!"
  - WARNING: "Parsowanie zakończone, ale mogą być błędy w sekundach: 12, 45, 78"
  - ERROR: "Parsowanie nie powiodło się. Spróbuj ponownie z innym plikiem."
- [ ] Przekierowanie do listy plików po pomyślnym parsowaniu (SUCCESS/WARNING)
```

### 5.3 Zarządzanie utworami

**US-06: Lista moich utworów**
```
JAKO zalogowany użytkownik
CHCĘ widzieć listę wszystkich swoich uploadowanych utworów
ABY móc je łatwo odnaleźć i pobrać

Kryteria akceptacji:
- [ ] Panel użytkownika wyświetla tabelę/listę utworów z kolumnami:
  - Tytuł utworu
  - Kompozytor
  - Data uploadu
  - Status (SUCCESS/WARNING/ERROR/PARSING)
- [ ] Utwory posortowane od najnowszych
- [ ] Możliwość kliknięcia w utwór aby zobaczyć szczegóły
- [ ] Pusta lista pokazuje komunikat "Nie masz jeszcze żadnych utworów"
```

**US-07: Szczegóły utworu i pobieranie plików**
```
JAKO zalogowany użytkownik
CHCĘ zobaczyć szczegóły utworu i pobrać pliki MIDI
ABY móc je odsłuchać na swoim komputerze

Kryteria akceptacji:
- [ ] Strona szczegółów wyświetla:
  - Tytuł, kompozytor, data uploadu
  - Status parsowania z odpowiednim komunikatem
  - Lista dostępnych plików do pobrania
- [ ] Dostępne pliki:
  - Oryginalny plik PDF (zawsze)
  - Plik MIDI całościowy (tylko jeśli status SUCCESS lub WARNING)
  - Pliki MIDI poszczególnych pięciolinii: staff_1.mid, staff_2.mid, etc.
- [ ] Każdy plik ma przycisk "Pobierz" z ikoną
- [ ] Kliknięcie pobiera plik na dysk użytkownika
- [ ] Przycisk "Powrót do listy utworów"
```

**US-08: Usuwanie utworu**
```
JAKO zalogowany użytkownik
CHCĘ móc usunąć swój utwór
ABY uporządkować swoją bibliotekę

Kryteria akceptacji:
- [ ] Przycisk "Usuń" przy każdym utworze na liście
- [ ] Potwierdzenie usunięcia (modal: "Czy na pewno chcesz usunąć?")
- [ ] Po usunięciu:
  - Usunięcie rekordu z bazy danych
  - Usunięcie wszystkich plików z dysku
  - Odświeżenie listy utworów
- [ ] Komunikat "Utwór został usunięty"
```

---

## 6. Functional Requirements

### 6.1 Moduł Auth (wykorzystuje Bootzooka)

**FR-AUTH-01: Rejestracja użytkownika**
- System przyjmuje: email (string, valid email format), password (string, min 8 znaków)
- Walidacja: email unikalny w bazie, hasło min. 8 znaków
- Hasło jest hashowane przed zapisem (bcrypt)
- Zwraca: userId + JWT token
- Brak weryfikacji emaila

**FR-AUTH-02: Logowanie użytkownika**
- System przyjmuje: email, password
- Weryfikuje credentials przeciwko bazie danych
- Zwraca: JWT token (ważny 24h)
- Token zawiera: userId, email, expiration

**FR-AUTH-03: Autoryzacja requestów**
- Wszystkie endpointy (poza /register, /login) wymagają JWT token w header `Authorization: Bearer {token}`
- System weryfikuje token i extraktuje userId
- Każdy endpoint działa tylko na danych należących do zalogowanego użytkownika

### 6.2 Moduł Upload

**FR-UPLOAD-01: Walidacja pliku**
- Akceptowane formaty: .pdf
- Maksymalny rozmiar: 10MB
- Reject z błędem 400 jeśli walidacja nie przeszła

**FR-UPLOAD-02: Zapis pliku**
- Struktura katalogów: `/storage/users/{userId}/uploads/{uploadId}/`
- Nazwa pliku: `original.pdf`
- uploadId: UUID generowane przez system
- Metadata zapisywane w PostgreSQL (tabela `uploads`)

**FR-UPLOAD-03: Inicjalizacja parsowania**
- Po zapisie pliku, system tworzy job w kolejce (in-memory queue)
- Status w DB: `PARSING`
- Zwraca użytkownikowi: uploadId, status: PARSING

### 6.3 Moduł Parsing (Audiveris)

**FR-PARSE-01: Job queue**
- System przetwarza joby asynchronicznie (FIFO)
- Jeden job na raz (sequential processing w MVP)
- Job zawiera: uploadId, userId, filePath

**FR-PARSE-02: Uruchomienie Audiveris CLI**
- System wywołuje Audiveris jako osobny proces CLI:
  ```bash
  audiveris -batch -export -output /storage/users/{userId}/uploads/{uploadId}/output/ /storage/users/{userId}/uploads/{uploadId}/original.pdf
  ```
- Timeout: 5 minut (jeśli dłużej → ERROR)
- Monitoring stdout/stderr procesu

**FR-PARSE-03: Analiza wyników**
- Audiveris generuje pliki:
  - `output.mid` (całościowy)
  - `output_staff_1.mid`, `output_staff_2.mid`, etc. (per pięciolinia)
- System sprawdza czy pliki zostały wygenerowane
- Jeśli pliki istnieją → status: SUCCESS
- Jeśli brak plików → status: ERROR

**FR-PARSE-04: Detekcja ostrzeżeń (WARNING)**
- Audiveris może zwracać ostrzeżenia w stdout/stderr
- System parsuje logi Audiveris w poszukiwaniu wzorców:
  - "warning", "uncertain", "low confidence"
- Jeśli ostrzeżenia znalezione → status: WARNING + lista timestampów (sekund)
- Timestampy ekstraktowane z logów Audiveris (jeśli dostępne)

**FR-PARSE-05: Obsługa błędów**
- Błędy parsowania (process exit code != 0) → status: ERROR
- Timeout → status: ERROR
- Brak outputu → status: ERROR
- Błąd zapisywany w DB z treścią z stderr

**FR-PARSE-06: Aktualizacja statusu**
- Po zakończeniu (SUCCESS/WARNING/ERROR) system aktualizuje rekord w DB
- Zapisuje: status, errorMessage (jeśli ERROR), warnings (jeśli WARNING)

### 6.4 Moduł Utworów (Uploads Management)

**FR-UPLOAD-MGT-01: Lista utworów użytkownika**
- Endpoint: `GET /api/uploads`
- Zwraca listę wszystkich uploadów dla zalogowanego użytkownika
- Sortowanie: od najnowszych (createdAt DESC)
- Pola: uploadId, title, composer, createdAt, status

**FR-UPLOAD-MGT-02: Szczegóły utworu**
- Endpoint: `GET /api/uploads/{uploadId}`
- Zwraca pełne detale: wszystkie pola + lista dostępnych plików
- Lista plików:
  - `original.pdf` (zawsze)
  - `output.mid` (jeśli status SUCCESS/WARNING)
  - `output_staff_N.mid` (jeśli istnieją)

**FR-UPLOAD-MGT-03: Status parsowania (polling)**
- Endpoint: `GET /api/uploads/{uploadId}/status`
- Zwraca: status, warnings (jeśli WARNING), errorMessage (jeśli ERROR)
- Frontend polluje co 5 sekund podczas PARSING

**FR-UPLOAD-MGT-04: Pobieranie plików**
- Endpoint: `GET /api/uploads/{uploadId}/files/{filename}`
- Weryfikacja: plik należy do zalogowanego użytkownika
- Zwraca plik jako binary stream z odpowiednimi headerami:
  - `Content-Type: application/pdf` lub `audio/midi`
  - `Content-Disposition: attachment; filename="{filename}"`

**FR-UPLOAD-MGT-05: Usuwanie utworu**
- Endpoint: `DELETE /api/uploads/{uploadId}`
- Weryfikacja: upload należy do zalogowanego użytkownika
- Usuwa:
  - Rekord z bazy danych
  - Katalog z dyskiem: `/storage/users/{userId}/uploads/{uploadId}/`
- Zwraca: 204 No Content

### 6.5 Moduł Storage

**FR-STORAGE-01: Zarządzanie plikami**
- Wszystkie pliki zapisywane lokalnie na dysku serwera
- Struktura:
  ```
  /storage/
    users/
      {userId}/
        uploads/
          {uploadId}/
            original.pdf
            output/
              output.mid
              output_staff_1.mid
              output_staff_2.mid
              ...
  ```

**FR-STORAGE-02: Brak limitów (MVP)**
- Brak limitu liczby uploadów na użytkownika
- Brak limitu całkowitego storage
- Brak automatycznego usuwania starych plików
- (Note: dla produkcji należałoby dodać limity)

---

## 7. Technical Architecture

### 7.1 Overview

```
┌─────────────────┐
│   User Browser  │
│   (React App)   │
└────────┬────────┘
         │ HTTPS
         ▼
┌─────────────────────────────────────┐
│    Backend (Scala 3 + Tapir)        │
│  ┌─────────────────────────────┐   │
│  │  API Layer (REST)           │   │
│  │  - Auth endpoints           │   │
│  │  - Upload endpoints         │   │
│  │  - Management endpoints     │   │
│  └──────────┬──────────────────┘   │
│             │                       │
│  ┌──────────▼──────────────────┐   │
│  │  Business Logic Layer       │   │
│  │  - Upload service           │   │
│  │  - Parsing service          │   │
│  │  - File service             │   │
│  └──────────┬──────────────────┘   │
│             │                       │
│  ┌──────────▼──────────────────┐   │
│  │  Job Queue (in-memory)      │   │
│  │  - Parsing jobs FIFO        │   │
│  └──────────┬──────────────────┘   │
│             │                       │
│             ▼                       │
│  ┌─────────────────────────────┐   │
│  │  Audiveris CLI Executor     │   │
│  │  (ProcessBuilder)           │   │
│  └─────────────────────────────┘   │
└─────────────┬───────────────────────┘
              │
         ┌────┴────┬─────────────┐
         ▼         ▼             ▼
    PostgreSQL   Local FS    Audiveris
    (metadata)   (files)     (CLI tool)
```

### 7.2 Technology Stack

#### Backend
- **Language:** Scala 3.7.3
- **Web Framework:** Tapir (REST API + OpenAPI generation)
- **HTTP Server:** http4s / Netty
- **Database:** PostgreSQL 15+
- **DB Access:** Slick / Doobie (w zależności od Bootzooka)
- **Auth:** JWT (jose4j lub similar)
- **Job Queue:** In-memory (Scala collections + Actor/Future)
- **Template:** Bootzooka (https://github.com/softwaremill/bootzooka)

#### Frontend
- **Framework:** React 18
- **Language:** TypeScript 5
- **UI Library:** shadcn/ui (Radix UI + Tailwind)
- **API Client:** Auto-generated z OpenAPI (openapi-typescript-codegen)
- **State Management:** React Context / useState (simple for MVP)
- **HTTP Client:** Fetch API / Axios
- **Build Tool:** Vite

#### Infrastructure
- **Containerization:** Docker + Docker Compose
- **Storage:** Local filesystem
- **OMR Engine:** Audiveris 5.7+ (https://github.com/Audiveris/audiveris)

### 7.3 Deployment Architecture (MVP)

```
Docker Host (local/server)
├── Container: backend
│   ├── Scala app (JVM 21+)
│   ├── Audiveris CLI installed
│   └── Volume: /storage → host:/data/storage
├── Container: frontend
│   └── nginx serving React build
└── Container: postgres
    └── Volume: /var/lib/postgresql/data → host:/data/postgres
```

### 7.4 Data Flow: Upload & Parsing

```
1. User submits form (PDF + metadata)
   ↓
2. Frontend: POST /api/uploads (multipart/form-data)
   ↓
3. Backend validates file (size, type)
   ↓
4. Backend saves PDF to /storage/users/{userId}/uploads/{uploadId}/original.pdf
   ↓
5. Backend creates record in DB (status: PARSING)
   ↓
6. Backend adds job to queue
   ↓
7. Backend returns: 202 Accepted + {uploadId, status: PARSING}
   ↓
8. Frontend starts polling: GET /api/uploads/{uploadId}/status (every 5s)
   ↓
9. Worker picks job from queue
   ↓
10. Worker executes Audiveris CLI:
    audiveris -batch -export /path/to/original.pdf
   ↓
11. Audiveris generates MIDI files in output/
   ↓
12. Worker checks results:
    - Files exist? → SUCCESS / WARNING (if warnings in logs)
    - No files / error? → ERROR
   ↓
13. Worker updates DB (status, warnings, errorMessage)
   ↓
14. Frontend receives updated status from polling
   ↓
15. Frontend shows result (SUCCESS/WARNING/ERROR) and redirects to upload details
```

### 7.5 Security Considerations

**Authentication & Authorization:**
- JWT tokens with expiration (24h)
- All endpoints (except /register, /login) require valid token
- userId extracted from token, used for data isolation

**File Upload Security:**
- File size limit enforced (10MB)
- File type validation (.pdf only)
- Unique directory per user (prevents path traversal)
- File download: verify ownership before serving

**Database:**
- Passwords hashed with bcrypt (already in Bootzooka)
- SQL injection prevention via prepared statements (Slick/Doobie)

**MVP Limitations (NOT implemented):**
- No rate limiting
- No CAPTCHA (bot protection)
- No file content scanning (malware)
- No HTTPS in local dev (only in production)

---

## 8. Data Model

### 8.1 Database Schema (PostgreSQL)

#### Table: `users` (from Bootzooka)
```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_email ON users(email);
```

#### Table: `uploads`
```sql
CREATE TABLE uploads (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  title VARCHAR(500) NOT NULL,
  composer VARCHAR(255),
  status VARCHAR(20) NOT NULL DEFAULT 'PARSING', 
    -- PARSING | SUCCESS | WARNING | ERROR
  warnings TEXT[], -- array of warning messages (if status = WARNING)
  error_message TEXT, -- error details (if status = ERROR)
  original_filename VARCHAR(255) NOT NULL,
  file_path VARCHAR(1000) NOT NULL, -- /storage/users/{userId}/uploads/{uploadId}/
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_uploads_user_id ON uploads(user_id);
CREATE INDEX idx_uploads_status ON uploads(status);
CREATE INDEX idx_uploads_created_at ON uploads(created_at DESC);
```

#### Table: `generated_files`
```sql
CREATE TABLE generated_files (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  upload_id UUID NOT NULL REFERENCES uploads(id) ON DELETE CASCADE,
  filename VARCHAR(255) NOT NULL, -- e.g., output.mid, output_staff_1.mid
  file_type VARCHAR(20) NOT NULL, -- MIDI_FULL | MIDI_STAFF | PDF
  file_path VARCHAR(1000) NOT NULL,
  file_size_bytes BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_generated_files_upload_id ON generated_files(upload_id);
```

### 8.2 Domain Models (Scala)

```scala
case class User(
  id: UserId,
  email: String,
  passwordHash: String,
  createdAt: Instant,
  updatedAt: Instant
)

case class Upload(
  id: UploadId,
  userId: UserId,
  title: String,
  composer: Option[String],
  status: UploadStatus,
  warnings: List[String],
  errorMessage: Option[String],
  originalFilename: String,
  filePath: String,
  createdAt: Instant,
  updatedAt: Instant
)

enum UploadStatus:
  case Parsing, Success, Warning, Error

case class GeneratedFile(
  id: FileId,
  uploadId: UploadId,
  filename: String,
  fileType: FileType,
  filePath: String,
  fileSizeBytes: Long,
  createdAt: Instant
)

enum FileType:
  case MidiFull, MidiStaff, Pdf
```

### 8.3 API DTOs (TypeScript)

```typescript
interface UploadRequest {
  title: string;
  composer?: string;
  file: File; // PDF file
}

interface UploadResponse {
  id: string; // uploadId
  status: 'PARSING' | 'SUCCESS' | 'WARNING' | 'ERROR';
}

interface UploadListItem {
  id: string;
  title: string;
  composer?: string;
  status: 'PARSING' | 'SUCCESS' | 'WARNING' | 'ERROR';
  createdAt: string; // ISO 8601
}

interface UploadDetail {
  id: string;
  title: string;
  composer?: string;
  status: 'PARSING' | 'SUCCESS' | 'WARNING' | 'ERROR';
  warnings: string[]; // if status = WARNING
  errorMessage?: string; // if status = ERROR
  originalFilename: string;
  createdAt: string;
  files: GeneratedFileInfo[];
}

interface GeneratedFileInfo {
  filename: string;
  fileType: 'MIDI_FULL' | 'MIDI_STAFF' | 'PDF';
  fileSizeBytes: number;
  downloadUrl: string; // /api/uploads/{uploadId}/files/{filename}
}

interface StatusResponse {
  status: 'PARSING' | 'SUCCESS' | 'WARNING' | 'ERROR';
  warnings?: string[];
  errorMessage?: string;
}
```

---

## 9. API Specification

### 9.1 Base URL
```
Local dev: http://localhost:8080/api/v1
Production: https://sheetmusicparser.com/api/v1
```

### 9.2 Authentication Endpoints (from Bootzooka)

#### POST /api/v1/auth/register
**Description:** Register a new user

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securepassword123"
}
```

**Response:** `201 Created`
```json
{
  "userId": "uuid",
  "email": "user@example.com",
  "token": "jwt.token.here"
}
```

**Errors:**
- `400 Bad Request` - Invalid email format or password too short
- `409 Conflict` - Email already exists

---

#### POST /api/v1/auth/login
**Description:** Login existing user

**Request:**
```json
{
  "email": "user@example.com",
  "password": "securepassword123"
}
```

**Response:** `200 OK`
```json
{
  "userId": "uuid",
  "email": "user@example.com",
  "token": "jwt.token.here"
}
```

**Errors:**
- `401 Unauthorized` - Invalid credentials

---

### 9.3 Upload Endpoints

#### POST /api/v1/uploads
**Description:** Upload a new PDF file for parsing

**Authentication:** Required (JWT token in Authorization header)

**Request:** `multipart/form-data`
```
title: string (required)
composer: string (optional)
file: binary (required, .pdf, max 10MB)
```

**Response:** `202 Accepted`
```json
{
  "id": "upload-uuid",
  "title": "Ave Maria",
  "status": "PARSING",
  "createdAt": "2025-10-26T10:30:00Z"
}
```

**Errors:**
- `400 Bad Request` - Missing title, invalid file type, file too large
- `401 Unauthorized` - Missing or invalid token

---

#### GET /api/v1/uploads
**Description:** Get list of all uploads for current user

**Authentication:** Required

**Query Parameters:**
- `limit` (optional, default: 50) - Number of items
- `offset` (optional, default: 0) - Pagination offset

**Response:** `200 OK`
```json
{
  "uploads": [
    {
      "id": "uuid",
      "title": "Ave Maria",
      "composer": "Franz Schubert",
      "status": "SUCCESS",
      "createdAt": "2025-10-26T10:30:00Z"
    },
    {
      "id": "uuid2",
      "title": "Hallelujah",
      "composer": "Leonard Cohen",
      "status": "PARSING",
      "createdAt": "2025-10-26T09:15:00Z"
    }
  ],
  "total": 2
}
```

**Errors:**
- `401 Unauthorized` - Missing or invalid token

---

#### GET /api/v1/uploads/{uploadId}
**Description:** Get detailed information about specific upload

**Authentication:** Required

**Path Parameters:**
- `uploadId` - UUID of the upload

**Response:** `200 OK`
```json
{
  "id": "uuid",
  "title": "Ave Maria",
  "composer": "Franz Schubert",
  "status": "SUCCESS",
  "warnings": [],
  "originalFilename": "ave_maria.pdf",
  "createdAt": "2025-10-26T10:30:00Z",
  "files": [
    {
      "filename": "original.pdf",
      "fileType": "PDF",
      "fileSizeBytes": 2457600,
      "downloadUrl": "/api/v1/uploads/uuid/files/original.pdf"
    },
    {
      "filename": "output.mid",
      "fileType": "MIDI_FULL",
      "fileSizeBytes": 45120,
      "downloadUrl": "/api/v1/uploads/uuid/files/output.mid"
    },
    {
      "filename": "output_staff_1.mid",
      "fileType": "MIDI_STAFF",
      "fileSizeBytes": 12800,
      "downloadUrl": "/api/v1/uploads/uuid/files/output_staff_1.mid"
    },
    {
      "filename": "output_staff_2.mid",
      "fileType": "MIDI_STAFF",
      "fileSizeBytes": 11500,
      "downloadUrl": "/api/v1/uploads/uuid/files/output_staff_2.mid"
    }
  ]
}
```

**Status = WARNING example:**
```json
{
  "id": "uuid",
  "title": "Complicated Piece",
  "status": "WARNING",
  "warnings": [
    "Parsowanie zakończone, ale mogą być błędy w sekundach: 12, 45, 78"
  ],
  "files": [...]
}
```

**Status = ERROR example:**
```json
{
  "id": "uuid",
  "title": "Broken PDF",
  "status": "ERROR",
  "errorMessage": "Audiveris could not recognize any musical symbols in the PDF",
  "files": [
    {
      "filename": "original.pdf",
      "fileType": "PDF",
      "downloadUrl": "/api/v1/uploads/uuid/files/original.pdf"
    }
  ]
}
```

**Errors:**
- `401 Unauthorized` - Missing or invalid token
- `403 Forbidden` - Upload belongs to different user
- `404 Not Found` - Upload doesn't exist

---

#### GET /api/v1/uploads/{uploadId}/status
**Description:** Get current status of parsing (for polling)

**Authentication:** Required

**Path Parameters:**
- `uploadId` - UUID of the upload

**Response:** `200 OK`
```json
{
  "status": "PARSING"
}
```

or

```json
{
  "status": "SUCCESS"
}
```

or

```json
{
  "status": "WARNING",
  "warnings": ["Parsowanie zakończone, ale mogą być błędy w sekundach: 12, 45"]
}
```

or

```json
{
  "status": "ERROR",
  "errorMessage": "Timeout: parsing took more than 5 minutes"
}
```

**Errors:**
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`

---

#### GET /api/v1/uploads/{uploadId}/files/{filename}
**Description:** Download a specific file (PDF or MIDI)

**Authentication:** Required

**Path Parameters:**
- `uploadId` - UUID of the upload
- `filename` - Name of the file (e.g., "original.pdf", "output.mid", "output_staff_1.mid")

**Response:** `200 OK`
- Binary file stream
- Headers:
  ```
  Content-Type: application/pdf (for PDF) or audio/midi (for MIDI)
  Content-Disposition: attachment; filename="ave_maria.pdf"
  Content-Length: 2457600
  ```

**Errors:**
- `401 Unauthorized`
- `403 Forbidden` - Upload belongs to different user
- `404 Not Found` - Upload or file doesn't exist

---

#### DELETE /api/v1/uploads/{uploadId}
**Description:** Delete an upload and all associated files

**Authentication:** Required

**Path Parameters:**
- `uploadId` - UUID of the upload

**Response:** `204 No Content`

**Errors:**
- `401 Unauthorized`
- `403 Forbidden` - Upload belongs to different user
- `404 Not Found`

---

## 10. User Flows

### 10.1 Flow: Nowy użytkownik rejestruje się i uploaduje pierwszy plik

```
1. User otwiera aplikację (/)
   → Widzi landing page z przyciskami "Zaloguj" i "Zarejestruj"

2. User klika "Zarejestruj"
   → Formularz rejestracji:
      [Email: _____________________]
      [Hasło: _____________________]
      [Powtórz hasło: _____________]
      [Przycisk: Zarejestruj się]

3. User wypełnia formularz i klika "Zarejestruj się"
   → POST /api/auth/register
   → Frontend otrzymuje token, zapisuje w localStorage
   → Przekierowanie do /dashboard

4. User widzi dashboard (pusta lista):
   "Nie masz jeszcze żadnych utworów. Rozpocznij od uploadowania pierwszego!"
   [Przycisk: + Dodaj nowy utwór]

5. User klika "+ Dodaj nowy utwór"
   → Przekierowanie do /upload

6. User widzi formularz uploadu:
   [Tytuł utworu: _____________________] *
   [Kompozytor: ________________________]
   [Przeciągnij plik PDF tutaj lub kliknij aby wybrać]
   [Przycisk: Uploaduj i parsuj]

7. User wypełnia tytuł: "Ave Maria"
   User wypełnia kompozytor: "Franz Schubert"
   User wybiera plik: ave_maria.pdf (3 MB)
   → Pokazuje się podgląd: "Wybrany plik: ave_maria.pdf (3.0 MB)"

8. User klika "Uploaduj i parsuj"
   → Progress bar uploadu: [=====>    ] 50%
   → POST /api/uploads (multipart)
   → Response: 202 + {id: "uuid", status: "PARSING"}

9. User widzi stronę parsowania:
   [Spinner animacja]
   "Parsowanie w toku..."
   "To może potrwać do 2 minut. Proszę czekać..."
   → Frontend polluje GET /api/uploads/uuid/status co 5s

10. Po 45 sekundach status zmienia się na SUCCESS
    → Frontend pokazuje:
       "✓ Parsowanie zakończone pomyślnie!"
       [Przycisk: Zobacz pliki]

11. User klika "Zobacz pliki"
    → Przekierowanie do /uploads/uuid

12. User widzi szczegóły utworu:
    "Ave Maria" by Franz Schubert
    Status: ✓ Sukces
    Data: 26 października 2025, 10:30

    Dostępne pliki do pobrania:
    📄 original.pdf (3.0 MB) [Pobierz]
    🎵 output.mid - Pełny utwór (44 KB) [Pobierz]
    🎵 output_staff_1.mid - Sopran (12 KB) [Pobierz]
    🎵 output_staff_2.mid - Alt (11 KB) [Pobierz]
    🎵 output_staff_3.mid - Tenor (10 KB) [Pobierz]
    🎵 output_staff_4.mid - Bas (11 KB) [Pobierz]

    [Przycisk: Powrót do listy]

13. User klika na "output_staff_2.mid - Alt [Pobierz]"
    → GET /api/uploads/uuid/files/output_staff_2.mid
    → Plik zostaje pobrany na dysk użytkownika

14. User może teraz odsłuchać swój głos (alt) w zewnętrznym odtwarzaczu MIDI
```

### 10.2 Flow: Parsowanie zakończone z ostrzeżeniami (WARNING)

```
1-8. [Jak w flow 10.1]

9. Po 90 sekundach status zmienia się na WARNING
   → Frontend pokazuje:
      "⚠ Parsowanie zakończone, ale mogą być błędy"
      "Potencjalne problemy w sekundach: 12, 45, 78"
      "Pliki MIDI zostały wygenerowane, ale zalecamy sprawdzenie ich poprawności."
      [Przycisk: Zobacz pliki]

10. User klika "Zobacz pliki"
    → Widzi listę plików (jak w 10.1 punkt 12)
    → Na górze jest warning box:
       "⚠ Uwaga: Parser wykrył potencjalne problemy"
       "Sprawdź pliki MIDI, szczególnie około sekund: 12, 45, 78"

11. User pobiera pliki i sprawdza je manualnie
```

### 10.3 Flow: Parsowanie nie powiodło się (ERROR)

```
1-8. [Jak w flow 10.1]

9. Po 120 sekundach status zmienia się na ERROR
   → Frontend pokazuje:
      "✗ Parsowanie nie powiodło się"
      "Audiveris nie mógł rozpoznać symboli muzycznych w pliku PDF."
      "Możliwe przyczyny:"
      " - Zbyt niska jakość skanu"
      " - Niestandard owy format zapisu nutowego"
      " - Uszkodzony plik PDF"
      [Przycisk: Spróbuj z innym plikiem]
      [Przycisk: Zobacz szczegóły]

10. User klika "Zobacz szczegóły"
    → Widzi stronę z detalami:
       Status: ✗ Błąd
       Komunikat: "Audiveris process exited with code 1: Could not detect any staff lines"
       
       Dostępne pliki:
       📄 original.pdf (3.0 MB) [Pobierz] ← User może pobrać oryginalny PDF

11. User klika "Spróbuj z innym plikiem"
    → Przekierowanie do /upload
    → User próbuje z lepszej jakości skanem
```

### 10.4 Flow: Zarządzanie utworami (lista, usuwanie)

```
1. User loguje się i otwiera /dashboard
   → Widzi listę swoich utworów:

   Moje utwory (5)
   [+ Dodaj nowy utwór]

   | Tytuł            | Kompozytor      | Data       | Status  | Akcje          |
   |------------------|-----------------|------------|---------|----------------|
   | Ave Maria        | Franz Schubert  | 26.10.2025 | ✓       | [Zobacz] [Usuń]|
   | Hallelujah       | Leonard Cohen   | 25.10.2025 | ✓       | [Zobacz] [Usuń]|
   | Stabat Mater     | Giovanni Pergo  | 24.10.2025 | ⚠       | [Zobacz] [Usuń]|
   | Requiem K.626    | W.A. Mozart     | 23.10.2025 | ⏳      | [Zobacz]       |
   | Bad Scan         | Unknown         | 22.10.2025 | ✗       | [Zobacz] [Usuń]|

2. User klika [Usuń] przy "Bad Scan"
   → Modal potwierdzenia:
      "Czy na pewno chcesz usunąć utwór 'Bad Scan'?"
      "Ta operacja jest nieodwracalna. Wszystkie pliki zostaną usunięte."
      [Anuluj] [Usuń]

3. User klika [Usuń]
   → DELETE /api/uploads/uuid
   → Modal znika
   → Lista odświeża się automatycznie (bez "Bad Scan")
   → Toast notification: "Utwór został usunięty"

4. User klika [Zobacz] przy "Hallelujah"
   → Przekierowanie do /uploads/uuid
   → Widzi szczegóły i pliki (jak w 10.1 punkt 12)
```

---

## 11. UI/UX Guidelines

### 11.1 Design Principles

**Minimalistyczny i czysty design:**
- Użycie bieli i szarości jako kolorów dominujących
- Akcenty w kolorze niebieskim (#3B82F6 - Tailwind blue-500)
- Dużo white space, jasna hierarchia wizualna
- Konsystentne odstępy (spacing scale: 4px, 8px, 16px, 24px, 32px)

**Desktop-first:**
- Optymalizacja dla rozdzielczości 1920x1080 i 1366x768
- Min. szerokość: 1024px
- Brak specjalnej optymalizacji mobile (może być nieoptymalne, ale nie zepsute)

**Accessibility (podstawy):**
- Kontrast kolorów zgodny z WCAG 2.1 AA
- Wszystkie przyciski i inputy mają focus states
- Alt texty dla ikon
- Semantyczny HTML

### 11.2 Component Library: shadcn/ui

Użycie gotowych komponentów z biblioteki [shadcn/ui](https://ui.shadcn.com/):
- Button
- Input
- Form (with validation)
- Table
- Card
- Alert / Toast
- Progress (for upload)
- Spinner / Loading
- Modal / Dialog
- Badge (for status indicators)

### 11.3 Layout Structure

```
┌─────────────────────────────────────────────────────────┐
│ Header (fixed top)                                      │
│ [Logo] SheetMusicParser          [User: email] [Wyloguj]│
├─────────────────────────────────────────────────────────┤
│                                                         │
│ Main Content Area (centered, max-width: 1200px)        │
│                                                         │
│                                                         │
│                                                         │
│                                                         │
└─────────────────────────────────────────────────────────┘
│ Footer (optional, centered)                            │
│ © 2025 SheetMusicParser - Projekt edukacyjny           │
└─────────────────────────────────────────────────────────┘
```

### 11.4 Key Screens Wireframes

#### Screen: Login / Register
```
┌─────────────────────────────────────┐
│         SheetMusicParser            │
│    [Logo lub ikona muzyczna]        │
│                                     │
│  ┌───────────────────────────────┐ │
│  │  [Zaloguj] [Zarejestruj]      │ │ ← Tabs
│  ├───────────────────────────────┤ │
│  │                               │ │
│  │  Email:                       │ │
│  │  [___________________________]│ │
│  │                               │ │
│  │  Hasło:                       │ │
│  │  [___________________________]│ │
│  │                               │ │
│  │         [Zaloguj się]         │ │
│  │                               │ │
│  └───────────────────────────────┘ │
└─────────────────────────────────────┘
```

#### Screen: Dashboard (lista utworów)
```
┌────────────────────────────────────────────────────────────┐
│ [Logo] SheetMusicParser    [User] [Wyloguj]               │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  Moje utwory (5)                    [+ Dodaj nowy utwór]  │
│                                                            │
│  ┌────────────────────────────────────────────────────┐   │
│  │ Tytuł  │ Kompozytor │ Data    │ Status │ Akcje    │   │
│  ├────────────────────────────────────────────────────┤   │
│  │ Ave    │ F. Schubert│ 26.10   │ ✓      │[↗][🗑]  │   │
│  │ Maria  │            │         │        │          │   │
│  ├────────────────────────────────────────────────────┤   │
│  │ Hall...│ L. Cohen   │ 25.10   │ ⚠      │[↗][🗑]  │   │
│  └────────────────────────────────────────────────────┘   │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

#### Screen: Upload form
```
┌────────────────────────────────────────────────────────────┐
│ [Logo] SheetMusicParser    [User] [Wyloguj]               │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ← Powrót        Dodaj nowy utwór                          │
│                                                            │
│  ┌──────────────────────────────────────────────────┐     │
│  │ Tytuł utworu: *                                  │     │
│  │ [____________________________________________]   │     │
│  │                                                  │     │
│  │ Kompozytor:                                      │     │
│  │ [____________________________________________]   │     │
│  │                                                  │     │
│  │ Plik PDF: *                                      │     │
│  │ ┌────────────────────────────────────────────┐ │     │
│  │ │  Przeciągnij plik PDF tutaj                │ │     │
│  │ │  lub                                        │ │     │
│  │ │         [Wybierz plik]                      │ │     │
│  │ │                                             │ │     │
│  │ │  Max. 10 MB                                 │ │     │
│  │ └────────────────────────────────────────────┘ │     │
│  │                                                  │     │
│  │ Wybrany: ave_maria.pdf (3.0 MB) ✓               │     │
│  │                                                  │     │
│  │              [Uploaduj i parsuj]                 │     │
│  └──────────────────────────────────────────────────┘     │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

#### Screen: Parsing in progress
```
┌────────────────────────────────────────────────────────────┐
│ [Logo] SheetMusicParser    [User] [Wyloguj]               │
├────────────────────────────────────────────────────────────┤
│                                                            │
│                      Ave Maria                             │
│                                                            │
│                    [Spinner animacja]                      │
│                                                            │
│                  Parsowanie w toku...                      │
│                                                            │
│          To może potrwać do 2 minut. Proszę czekać.       │
│                                                            │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

#### Screen: Upload detail (SUCCESS)
```
┌────────────────────────────────────────────────────────────┐
│ [Logo] SheetMusicParser    [User] [Wyloguj]               │
├────────────────────────────────────────────────────────────┤
│                                                            │
│  ← Powrót do listy                           [Usuń utwór] │
│                                                            │
│  Ave Maria                                                 │
│  Kompozytor: Franz Schubert                                │
│  Data: 26 października 2025, 10:30                         │
│  Status: ✓ Parsowanie zakończone pomyślnie                │
│                                                            │
│  ─────────────────────────────────────────────────────    │
│                                                            │
│  Dostępne pliki do pobrania:                               │
│                                                            │
│  📄 original.pdf (3.0 MB)              [Pobierz ⬇]        │
│  🎵 output.mid - Pełny utwór (44 KB)   [Pobierz ⬇]        │
│  🎵 output_staff_1.mid - Sopran (12KB) [Pobierz ⬇]        │
│  🎵 output_staff_2.mid - Alt (11 KB)   [Pobierz ⬇]        │
│  🎵 output_staff_3.mid - Tenor (10 KB) [Pobierz ⬇]        │
│  🎵 output_staff_4.mid - Bas (11 KB)   [Pobierz ⬇]        │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

### 11.5 Status Indicators

**Visual design dla statusów:**

| Status   | Icon | Color    | Text                                      |
|----------|------|----------|-------------------------------------------|
| PARSING  | ⏳   | Gray     | "Parsowanie w toku..."                    |
| SUCCESS  | ✓    | Green    | "Parsowanie zakończone pomyślnie"         |
| WARNING  | ⚠    | Yellow   | "Parsowanie zakończone, ale mogą być błędy"|
| ERROR    | ✗    | Red      | "Parsowanie nie powiodło się"             |

**Kolory (Tailwind CSS):**
- SUCCESS: `text-green-600`, `bg-green-50`, `border-green-200`
- WARNING: `text-yellow-600`, `bg-yellow-50`, `border-yellow-200`
- ERROR: `text-red-600`, `bg-red-50`, `border-red-200`
- PARSING: `text-gray-600`, `bg-gray-50`, `border-gray-200`

---

## 12. Out of Scope (MVP)

Poniższe funkcjonalności **NIE** wchodzą w zakres MVP i mogą być rozważone w przyszłych iteracjach:

### 12.1 Funkcjonalności produktowe
- ❌ Odtwarzanie MIDI w przeglądarce (web player)
- ❌ Podgląd nut podczas odtwarzania (score following)
- ❌ Korekta / edycja wyników parsowania
- ❌ Eksport do innych formatów (MP3, WAV, MusicXML)
- ❌ Współdzielenie utworów z innymi użytkownikami
- ❌ Publiczna galeria utworów
- ❌ Komentarze, oceny, polubienia
- ❌ Organizacja w foldery / tagi / playlists
- ❌ Social login (Google, Facebook)
- ❌ Email verification
- ❌ Password reset / forgot password
- ❌ User profile / settings
- ❌ Zmiana hasła
- ❌ Usuwanie konta
- ❌ Dark mode

### 12.2 Techniczne
- ❌ Responsywność mobile / tablet
- ❌ Progressive Web App (PWA)
- ❌ Internationalization (i18n) - tylko polski w MVP
- ❌ Real-time updates (WebSockets) - używamy pollingu
- ❌ Advanced search / filtering
- ❌ Batch upload (wiele plików naraz)
- ❌ Cloud storage (S3, GCS) - tylko lokalny dysk
- ❌ CDN dla plików
- ❌ Rate limiting / throttling
- ❌ Monitoring / observability (Prometheus, Grafana)
- ❌ Automated backups
- ❌ CI/CD pipelines
- ❌ E2E tests (tylko integration tests)
- ❌ Performance optimization (caching, CDN)

### 12.3 Skalowanie i produkcja
- ❌ Horizontal scaling (load balancer, multiple instances)
- ❌ Queue persistence (Redis, RabbitMQ) - tylko in-memory
- ❌ Multi-region deployment
- ❌ HTTPS / SSL certificates (tylko HTTP w local dev)
- ❌ Domain + hosting setup
- ❌ Analytics / tracking (Google Analytics)
- ❌ Error tracking (Sentry)
- ❌ Limity użytkowników (storage, liczba utworów)
- ❌ Soft delete (obecnie hard delete)

---

## 13. Development Phases

### Phase 0: Setup & Infrastructure (Week 1)
**Goal:** Przygotowanie środowiska developerskiego

**Tasks:**
- [ ] Fork/clone Bootzooka template
- [ ] Rename project to `sheet-music-app`
- [ ] Setup PostgreSQL (Docker container)
- [ ] Verify backend runs (`./backend-start.sh`)
- [ ] Verify frontend runs (`./frontend-start.sh`)
- [ ] Setup Audiveris CLI:
  - Download Audiveris 5.7+ binary
  - Test CLI locally: `audiveris -batch -export sample.pdf`
  - Document installation process
- [ ] Create `.ai/` directory with PRD and project context
- [ ] Initial commit to git

**Acceptance Criteria:**
- Backend serves on `localhost:8080`
- Frontend serves on `localhost:8081`
- Database connects successfully
- Audiveris CLI runs and generates MIDI from test PDF
- All code in version control

---

### Phase 1: Backend - Upload Module (Week 2)
**Goal:** Implementacja uploadu plików PDF z podstawową walidacją

**Tasks:**
- [ ] Create `uploads` table migration
- [ ] Create `generated_files` table migration
- [ ] Implement `UploadService`:
  - Validate file (type, size)
  - Save PDF to filesystem (`/storage/users/{userId}/uploads/{uploadId}/`)
  - Insert record to DB (status: PARSING)
- [ ] Implement `POST /api/uploads` endpoint (Tapir)
- [ ] Implement `GET /api/uploads` endpoint (list)
- [ ] Implement `GET /api/uploads/{id}` endpoint (detail)
- [ ] Add authorization checks (userId from JWT)
- [ ] Write integration tests:
  - Upload valid PDF → 202
  - Upload too large file → 400
  - Upload non-PDF → 400
  - Upload without auth → 401
- [ ] Update OpenAPI spec (auto-generated by Tapir)

**Acceptance Criteria:**
- Can upload PDF via API, file saved to disk + DB record created
- Authorization works (users see only their uploads)
- Tests pass (green)
- OpenAPI spec updated

---

### Phase 2: Backend - Parsing Module (Week 3)
**Goal:** Integracja z Audiveris CLI i asynchroniczne parsowanie

**Tasks:**
- [ ] Implement in-memory job queue:
  - Queue data structure (FIFO)
  - Worker actor/thread that processes jobs
- [ ] Implement `AudiverisService`:
  - Execute Audiveris CLI via `ProcessBuilder`
  - Command: `audiveris -batch -export -output {outputDir} {inputPdf}`
  - Capture stdout/stderr
  - Timeout after 5 minutes
- [ ] Implement result analyzer:
  - Check if MIDI files generated
  - Parse logs for warnings
  - Determine status: SUCCESS / WARNING / ERROR
- [ ] Update `UploadService`:
  - After upload, enqueue parsing job
- [ ] Implement status updates:
  - Worker updates DB after parsing completes
- [ ] Implement `GET /api/uploads/{id}/status` endpoint
- [ ] Write integration tests:
  - Mock Audiveris CLI
  - Test SUCCESS scenario
  - Test WARNING scenario
  - Test ERROR scenario (timeout, no output)

**Acceptance Criteria:**
- Upload triggers async parsing job
- Audiveris generates MIDI files
- Status correctly updated in DB (SUCCESS/WARNING/ERROR)
- Tests pass with mocked Audiveris

---

### Phase 3: Backend - File Management (Week 4)
**Goal:** Download plików i zarządzanie utworami

**Tasks:**
- [ ] Implement file listing:
  - Scan `/storage/.../output/` for MIDI files
  - Populate `generated_files` table after parsing
- [ ] Implement `GET /api/uploads/{id}/files/{filename}` endpoint:
  - Verify user ownership
  - Stream file binary
  - Set correct headers (Content-Type, Content-Disposition)
- [ ] Implement `DELETE /api/uploads/{id}` endpoint:
  - Delete DB record (cascade to generated_files)
  - Delete filesystem directory (recursively)
- [ ] Write integration tests:
  - Download PDF → receives file
  - Download MIDI → receives file
  - Download file from other user → 403
  - Delete upload → DB + files removed

**Acceptance Criteria:**
- Can download all files (PDF, MIDI)
- Can delete upload (DB + files)
- Authorization works
- Tests pass

---

### Phase 4: Frontend - Auth & Layout (Week 5)
**Goal:** UI dla rejestracji, logowania i podstawowy layout

**Tasks:**
- [ ] Setup shadcn/ui components:
  - Install and configure Tailwind CSS (already in Bootzooka?)
  - Add: Button, Input, Form, Card, Alert
- [ ] Create layout components:
  - `Header.tsx` (logo, user email, logout button)
  - `Layout.tsx` (wraps pages with Header)
- [ ] Implement Auth pages:
  - `LoginPage.tsx` (form: email, password)
  - `RegisterPage.tsx` (form: email, password, repeat password)
- [ ] Implement auth context:
  - Store JWT token in localStorage
  - Provide `useAuth()` hook
  - Auto-logout on 401
- [ ] Implement routing (React Router):
  - `/` → LoginPage (if not logged in) or redirect to /dashboard
  - `/register` → RegisterPage
  - `/dashboard` → Dashboard (protected)
- [ ] API client setup:
  - Generate TypeScript client from OpenAPI spec (openapi-typescript-codegen)
  - Configure base URL from `.env`

**Acceptance Criteria:**
- Can register new user → auto login
- Can login existing user → redirects to dashboard
- Can logout → clears token, redirects to login
- Protected routes redirect to login if not authenticated

---

### Phase 5: Frontend - Upload Flow (Week 6)
**Goal:** UI dla uploadu plików i monitorowania statusu

**Tasks:**
- [ ] Create `UploadPage.tsx`:
  - Form: title (required), composer (optional), file picker
  - Drag & drop support
  - File validation (client-side: .pdf, max 10MB)
  - Upload progress bar
- [ ] Implement upload logic:
  - `POST /api/uploads` with multipart/form-data
  - Show progress bar during upload
  - On success (202) → redirect to parsing status page
  - On error (400) → show error message
- [ ] Create `ParsingStatusPage.tsx`:
  - Display spinner + "Parsowanie w toku..."
  - Poll `GET /api/uploads/{id}/status` every 5 seconds
  - On SUCCESS → show success message + redirect to detail page after 2s
  - On WARNING → show warning message + redirect
  - On ERROR → show error message + button "Spróbuj ponownie"
- [ ] Add shadcn/ui components:
  - Progress (upload bar)
  - Spinner (parsing)

**Acceptance Criteria:**
- Can upload PDF via UI
- See progress bar during upload
- Automatically poll status during parsing
- Redirected to detail page when done

---

### Phase 6: Frontend - Dashboard & Upload Management (Week 7)
**Goal:** Lista utworów, szczegóły, pobieranie, usuwanie

**Tasks:**
- [ ] Create `DashboardPage.tsx`:
  - Fetch `GET /api/uploads`
  - Display table: title, composer, date, status, actions
  - Status badge with colors (green/yellow/red/gray)
  - Actions: "Zobacz" button, "Usuń" button
  - "+ Dodaj nowy utwór" button → /upload
- [ ] Create `UploadDetailPage.tsx`:
  - Fetch `GET /api/uploads/{id}`
  - Display: title, composer, date, status with message
  - List of files with download buttons
  - "Powrót do listy" button
  - "Usuń utwór" button
- [ ] Implement file download:
  - Click download → `GET /api/uploads/{id}/files/{filename}`
  - Browser automatically downloads file
- [ ] Implement upload deletion:
  - Click "Usuń" → show confirmation modal
  - On confirm → `DELETE /api/uploads/{id}`
  - Redirect to dashboard + show toast "Utwór został usunięty"
- [ ] Add shadcn/ui components:
  - Table
  - Badge (status)
  - Dialog (delete confirmation)
  - Toast (notifications)

**Acceptance Criteria:**
- Dashboard shows all user's uploads
- Can view upload details
- Can download all files (PDF, MIDI)
- Can delete upload with confirmation
- Toast notifications work

---

### Phase 7: Polish & Testing (Week 8)
**Goal:** Dopracowanie UI, testy, dokumentacja

**Tasks:**
- [ ] UI polish:
  - Consistent spacing and typography
  - Error states for all forms
  - Loading states for all async operations
  - Empty states ("Nie masz jeszcze utworów")
  - Add favicon, page titles
- [ ] Frontend error handling:
  - Display user-friendly errors (not raw API errors)
  - Handle network errors (show "Błąd połączenia z serwerem")
  - Handle auth errors (auto logout on 401)
- [ ] Backend logging:
  - Structured logs for parsing jobs (start, end, status, duration)
  - Error logs with stack traces
- [ ] Manual testing:
  - Test complete user journey (register → upload → download → delete)
  - Test with various PDFs (good quality, poor quality, invalid)
  - Test error scenarios (too large file, timeout, wrong format)
- [ ] Documentation:
  - Update README with setup instructions
  - Document Audiveris installation
  - Document local development workflow
- [ ] Code cleanup:
  - Remove unused code
  - Format code (scalafmt, prettier)
  - Fix linter warnings

**Acceptance Criteria:**
- UI is polished and consistent
- All error cases handled gracefully
- Manual testing scenarios pass
- README is complete and accurate
- Code is clean and formatted

---

### Phase 8: Docker & Deployment (Week 9)
**Goal:** Konteneryzacja i deploy lokalny

**Tasks:**
- [ ] Create `Dockerfile` for backend:
  - Base image: Eclipse Temurin JDK 21
  - Install Audiveris
  - Copy built JAR
  - Expose port 8080
- [ ] Create `Dockerfile` for frontend:
  - Multi-stage: build (node) + serve (nginx)
  - Copy built React app
  - Expose port 80
- [ ] Update `docker-compose.yml`:
  - Service: postgres (volume for data)
  - Service: backend (depends on postgres, volume for /storage)
  - Service: frontend (depends on backend)
- [ ] Environment variables:
  - Backend: `DATABASE_URL`, `JWT_SECRET`, `STORAGE_PATH`
  - Frontend: `REACT_APP_API_URL`
- [ ] Test Docker setup:
  - `docker-compose up`
  - Verify all services start
  - Test complete flow in Docker environment
- [ ] Documentation:
  - Document Docker setup in README
  - Document environment variables

**Acceptance Criteria:**
- Can start entire app with `docker-compose up`
- All functionality works in Docker
- Data persists across restarts (volumes)
- Documentation complete

---

## 14. Risks & Mitigations

### 14.1 Technical Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| **Audiveris integration failures** - CLI może nie działać jak oczekiwano, niekompatybilność wersji | High | High | - Prototypować integrację w Phase 0<br>- Przygotować fallback: prosty error message<br>- Dokumentować dokładnie działającą wersję Audiveris |
| **Audiveris accuracy issues** - Parser może mieć niski success rate na realnych PDF-ach | High | Medium | - MVP zakłada 100% poprawność (out of scope: korekta)<br>- Zbierać statystyki (success rate) w logach<br>- Komunikować użytkownikowi że parser nie jest idealny (WARNING status) |
| **Parsing timeout** - Duże pliki PDF mogą przekraczać 5 minut | Medium | Low | - Zwiększyć timeout jeśli potrzeba<br>- Pokazać użytkownikowi estymowany czas<br>- Rozważyć limit stron PDF (np. max 20 stron) |
| **Storage space** - Brak limitów może prowadzić do zapełnienia dysku | Medium | Medium | - Monitorować użycie dysku w logach<br>- Dodać alert jeśli dysk pełny<br>- Post-MVP: wprowadzić limity per user |
| **Performance issues** - Sequential processing może być wolne przy wielu użytkownikach | Low | Medium | - MVP: tylko jeden użytkownik/tester<br>- Post-MVP: parallel workers lub zewnętrzny queue (Redis) |

### 14.2 Project Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| **LLM limitations** - Claude może nie znać Scala 3 / Audiveris dobrze | Medium | Medium | - Dzielić zadania na małe kawałki<br>- Weryfikować kod manualnie<br>- Mieć dokumentację pod ręką (Tapir, Audiveris)<br>- Iterować: feedback loop z LLM |
| **Scope creep** - Chęć dodawania funkcji poza MVP | Medium | Low | - Trzymać się PRD strictly<br>- Lista "Out of Scope" jako reminder<br>- Focus na działającym MVP, nie idealnym produkcie |
| **Time underestimation** - 9 tygodni może nie wystarczyć | Medium | Low | - Projekt edukacyjny bez deadline'u<br>- Można pominąć Phase 8 (Docker) jeśli brak czasu<br>- Priorytet: funkcjonalność > deployment |

### 14.3 User Experience Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| **Poor PDF quality** - Użytkownicy uploadują skany niskiej jakości | High | Medium | - Komunikować wymagania jakości w UI<br>- Pokazać przykład dobrego PDF<br>- Status ERROR z pomocnym komunikatem |
| **Confusion about MIDI files** - Użytkownicy nie wiedzą który plik wybrać | Medium | Low | - Nazwy plików opisowe (staff_1 = Sopran)<br>- Post-MVP: detekcja głosów automatycznie<br>- Dodać tooltips / helptexty |
| **Long wait times** - 2 minuty parsowania może być frustrujące | Medium | Low | - Pokazać progress / spinner<br>- Komunikować estymowany czas<br>- Możliwość zamknięcia okna (polling w tle) |

---

## 15. Appendices

### 15.1 Glossary

**Terminy techniczne:**
- **OMR (Optical Music Recognition)** - Automatyczne rozpoznawanie zapisu nutowego z obrazów/PDF-ów
- **MIDI (Musical Instrument Digital Interface)** - Format plików zawierający informacje o nutach (wysokość, długość, głośność)
- **JWT (JSON Web Token)** - Standard tokenów autoryzacyjnych
- **Tapir** - Biblioteka Scala do definiowania API z auto-generacją OpenAPI
- **Bootzooka** - Template projekt Scala + React od SoftwareMill
- **shadcn/ui** - Kolekcja komponentów React (Radix UI + Tailwind CSS)

**Terminy muzyczne:**
- **Pięciolinia (staff)** - 5 linii na których zapisywane są nuty
- **Głos (voice)** - Partia dla jednej grupy śpiewaków (sopran, alt, tenor, bas)
- **Partytura (score)** - Kompletny zapis nutowy utworu dla wszystkich głosów

### 15.2 References

**Projekt bazowy:**
- Bootzooka: https://github.com/softwaremill/bootzooka
- Bootzooka docs: https://softwaremill.github.io/bootzooka/

**OMR Engine:**
- Audiveris: https://github.com/Audiveris/audiveris
- Audiveris handbook: https://audiveris.github.io/audiveris/

**Frontend stack:**
- React: https://react.dev/
- shadcn/ui: https://ui.shadcn.com/
- Tailwind CSS: https://tailwindcss.com/

**Backend stack:**
- Scala 3: https://docs.scala-lang.org/scala3/
- Tapir: https://tapir.softwaremill.com/
- http4s: https://http4s.org/

### 15.3 Contact & Ownership

**Project Owner:** Piotr Kukla  
**Repository:** `/Users/pkukla/development/przeprogramowani/sheet-music-app`  
**Type:** Educational project  
**Timeline:** Flexible (no hard deadline)

---

## Document History

| Version | Date       | Author         | Changes                           |
|---------|------------|----------------|-----------------------------------|
| 1.0     | 2025-10-26 | Product Manager| Initial PRD creation             |

---

**END OF DOCUMENT**

