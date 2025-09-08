# Aula0408_FireBase

## Aplicação em kotlin com FireBase
App integrado ao firebase, utilizand Authentication e Realtime Database.


## Interfaces
Criação destas interface no mainActivity:

Login
![WhatsApp Image 2025-09-01 at 16 26 11 (2)](https://github.com/user-attachments/assets/1f233d1e-eb56-404c-a0f0-dc5f20c87755)

Registro 
![WhatsApp Image 2025-09-01 at 16 26 11 (1)](https://github.com/user-attachments/assets/b91df8f7-d8fd-4ae1-8f36-b8e5561daae5)

Home (com registros cadastrados)
![WhatsApp Image 2025-09-01 at 16 26 11 = 100x](https://github.com/user-attachments/assets/0398df09-ff5c-44eb-bd46-166979e37b2e)


## Banco 


• Criar um banco com a plataforma firebase em : [https://firebase.google.com/] 
com o mesmo nome da aplicação.

• Adição das bibliotecas Authentication, Firestore e Realtime Database.
 
• No Authentication, habilitar login com email e senha, e  regras de segurança públicas no Realtime Database e no Firestore.

• Conectar o app ao Firebase no sanduíche da IDE -> Tools -> Firebase:
Indo nas funções:
Conectar o Authentication, o Firestore e o Realtime Database ao app, visando adicionar também suas SDKs ao projeto.

• Instânciando no MainActivity:
"val db = Firebase.firestore"
colocando o banco com firestore no objetivo bd.
