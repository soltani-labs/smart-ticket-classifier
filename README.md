Smart Ticket Classifier:
Un petit système de support que j'ai fait avec Spring Boot. Au lieu de classifier les tickets à la main, ça utilise une IA (Google Gemini) pour trouver automatiquement la catégorie, la priorité et un petit résumé dès qu'un ticket arrive.

Pourquoi j'ai créer ça
La plupart des sytèmes de tickets que j'ai vu te laissent juste tout taguer manuellement. Je voulais essayer de brancher un vrai modèle d'IA dans un vrai backend pour voir ce que ça donne du genre : qu'est-ce qui se passe si l'appel IA est lent, qu'est-ce qui se passe si ça plante, et comment on laisse un humain corriger si l'IA se trompe. Ce projet c'est un peu ma façon de répondre à ça.

Ce que ça fait
Tu envoies un ticket (sujet, description, email)

En arrière-plan, Gemini le lit et décide :

Catégorie → billing / technical / account / other

Priorité → low / medium / high / urgent

Un court résumé sur une ligne

L'API attend pas après l'IA, elle répond tout de suite et classifie le ticket en tâche de fond

Tu peux filtrer les tickets par catégorie, priorité ou status

Si l'IA se trompe, un agent du support peut modifier — et l'appli garde une trace de ce que l'IA avait deviné au début par rapport au résultat final

Y a un endpoint de stats simple qui montre à quelle fréquence l'IA avait raison (c'est-à-dire pas modifiée)

Si l'appel IA plante (mauvaise connexion, API down, etc.), ça réessaie deux-trois fois, et si ça marche toujours pas, le ticket est juste marqué "unclassified" au lieu d'être perdu

Stack
Java 21 + Spring Boot

Spring Web + Spring Data JPA

PostgreSQL

Spring Retry (pour gérer les plantages des appels IA)

Google Gemini API (version gratuite, appelée direct en REST, pas de SDK)

Structure du projet
Plaintext
com.proj.ticket_system
 ├── entity         -> Ticket, TicketClassification, et les enums
 ├── dto            -> objets request/response
 ├── repository     -> JPA repositories
 ├── service        -> la logique + l'appel Gemini
 ├── controller     -> les endpoints REST
 └── config         -> la config pour appeler l'API Gemini

 ***********************************************************************

-Application.yaml avec tes identifiants de BDD :
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

  *********************************************************************

Endpoints:
POST /tickets — envoyer un nouveau ticket

GET /tickets — lister les tickets (on peut filtrer avec ?category=, ?priority=, ?status=)

GET /tickets/{id} — récupérer un ticket

PATCH /tickets/{id}/classification — modifier la classification de l'IA

GET /stats/accuracy — voir si l'IA a été précise jusqu'ici
