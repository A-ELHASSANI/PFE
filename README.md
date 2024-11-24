# PFE
Projet de fin d'étude pour la gestion d'hotel

## Configuration de l'application

- Application monolithique
- Base de données PostgreSQL pour le développement et la production .
- Angular 15
- JWT authentification
- Maven
- Swagger pour l'api management 
- Developper sur Spring tools suite 4



## Utilisateurs de l'application
L'application dispose de 2 types d'utilisateurs:

- User : reservateur 
- Admnistrateur

#### Admnistrateur
L'administrateur dispose d'un onglet "administration" qui lui permet de controler divers paramètres de l'application.

### Comptes

<ul>
    <li>User:</li>
    <ul>
        <li>Login: user@example.com </li>
        <li>mot de passe: user123 </li>
    </ul>
</ul>
<ul>
    <li>User:</li>
    <ul>
        <li>Login: anas@gmail.com </li>
        <li>mot de passe: anas@123 </li>
    </ul>
</ul>
<ul>
    <li>Admin:</li>
    <ul>
        <li>Login: admin@example.com</li>
        <li>mot de passe: admin</li>
    </ul>
</ul>

### Remarque

Lors qu'un user est créé depuis un compte administrateur son mot de passe est par défaut: 1234
