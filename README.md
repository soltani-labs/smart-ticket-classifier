<div align="center">

#  Smart Ticket Classifier

### Un petit système de support avec Spring Boot + Google Gemini

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-brightgreen?style=for-the-badge\&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge\&logo=postgresql)
![Gemini](https://img.shields.io/badge/Google%20Gemini-AI-purple?style=for-the-badge\&logo=google)

</div>

---

## 🎫 Smart Ticket Classifier

Un petit système de support que j'ai fait avec **Spring Boot**.

Au lieu de classifier les tickets à la main, ça utilise une **IA (Google Gemini)** pour trouver automatiquement :

*  la catégorie
*  la priorité
*  un petit résumé

dès qu'un ticket arrive.

---

##  Pourquoi j'ai créé ça

La plupart des systèmes de tickets que j'ai vu te laissent juste tout taguer manuellement.

Je voulais essayer de brancher un **vrai modèle d'IA dans un vrai backend** pour voir ce que ça donne du genre :

> *qu'est-ce qui se passe si l'appel IA est lent ?*
> *qu'est-ce qui se passe si ça plante ?*
> *et comment on laisse un humain corriger si l'IA se trompe ?*

Ce projet c'est un peu ma façon de répondre à ça.

---

##  Ce que ça fait

Tu envoies un ticket :

```text
Sujet
Description
Email
```

↓

L'application l'enregistre et **Gemini l'analyse en arrière-plan**.

↓

L'IA décide :

|                  |                                               |
| ---------------- | --------------------------------------------- |
|  **Catégorie** | `billing` / `technical` / `account` / `other` |
|  **Priorité**   | `low` / `medium` / `high` / `urgent`          |
|  **Résumé**    | Un court résumé sur une ligne                 |

---

###  Traitement en arrière-plan

L'API n'attend pas après l'IA, elle répond tout de suite et **classifie le ticket en tâche de fond**.

```text
       Ticket
          │
          ▼
   ┌─────────────┐
   │ Spring Boot │
   └──────┬──────┘
          │
          ├──────────────► Réponse immédiate
          │
          ▼
    Google Gemini
          │
          ▼
 ┌────────────────────┐
 │ Category            │
 │ Priority            │
 │ Summary             │
 └────────────────────┘
```

---

###  Filtrer les tickets

Tu peux filtrer les tickets par :

```text
category
priority
status
```

---

###  Correction humaine

Si l'IA se trompe, un agent du support peut modifier.

Et l'appli garde une trace de :

```text
┌──────────────────────────┐
│ Classification IA        │
└────────────┬─────────────┘
             │
             ▼
       Correction humaine
             │
             ▼
┌──────────────────────────┐
│ Résultat final           │
└──────────────────────────┘
```

Ça permet de comparer ce que **l'IA avait deviné au début** par rapport au **résultat final**.

---

###  Statistiques

Y a un endpoint de stats simple qui montre à quelle fréquence l'IA avait raison.

C'est-à-dire : **pas modifiée par l'humain**.

---

### 🔁 Et si l'IA plante ?

Si l'appel IA plante :

```text
Mauvaise connexion
       │
       ▼
   Gemini API
       │
       ✕
       │
       ▼
     Retry
       │
       ▼
     Retry
       │
       ▼
 Toujours en erreur ?
       │
       ▼
  UNCLASSIFIED
```

Ça réessaie deux-trois fois, et si ça marche toujours pas, le ticket est juste marqué :

```text
UNCLASSIFIED
```

au lieu d'être perdu.

---

##  Avec quoi c'est fait

| Technologie              | Utilisation                         |
| ------------------------ | ----------------------------------- |
|  **Java 21**            | Backend                             |
|  **Spring Boot**       | Framework                           |
|  **Spring Web**        | API REST                            |
|  **Spring Data JPA**  | Accès aux données                   |
|  **PostgreSQL**        | Base de données                     |
|  **Spring Retry**      | Gestion des plantages des appels IA |
|  **Google Gemini API** | Classification IA                   |

Gemini est appelé directement en **REST**, pas avec un SDK.

---

## 📁 Structure du projet

```text
com.proj.ticket_system
│
├── entity
│   └── Ticket, TicketClassification, et les enums
│
├── dto
│   └── objets request/response
│
├── repository
│   └── JPA repositories
│
├── service
│   └── la logique + l'appel Gemini
│
├── controller
│   └── les endpoints REST
│
└── config
    └── la config pour appeler l'API Gemini
```

---

## ⚙️ Configuration

`application.yaml` avec tes identifiants de BDD :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tickets
    username: postgres
    password: "ton_mot_de_passe"

  jpa:
    hibernate:
      ddl-auto: update

llm:
  api-key: ${GEMINI_API_KEY}
  api-url: https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent
```

> ⚠️ **Important :** ne mets jamais ta vraie clé Gemini directement dans GitHub.

---

##  Endpoints

| Méthode | Endpoint                       | Description                          |
| ------- | ------------------------------ | ------------------------------------ |
| `POST`  | `/tickets`                     | Envoyer un nouveau ticket            |
| `GET`   | `/tickets`                     | Lister les tickets                   |
| `GET`   | `/tickets/{id}`                | Récupérer un ticket                  |
| `PATCH` | `/tickets/{id}/classification` | Modifier la classification de l'IA   |
| `GET`   | `/stats/accuracy`              | Voir si l'IA a été précise jusqu'ici |

### Filtres

```text
GET /tickets?category=technical
GET /tickets?priority=high
GET /tickets?status=OPEN
```

---

<div align="center">

### 🤖 Spring Boot × Google Gemini

Petit projet perso pour expérimenter l'intégration d'une IA dans un backend Java.

</div>
