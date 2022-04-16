# Database

## Introduction

Features we aim in this database :
1. Security
1. Easy to use
1. No SQL query
1. 100% **Kotlin**

### Security

The security in guarantee by the database is encrypted with **Android Key Store**.

So that only the application is able to read/write/modify/delete the data

So if a hacker manage to get the database file,, he have to also hack **Android Key Store**

### Easy to use and no SQL query

You will not have to care about SQL queries, they are created for you

As we will explain in more details, the database base store `fr.jhelp.database.DatabaseObject`. 
Just create one to start store. 

Query to get stored objects are **DSL** that manipulates the `fr.jhelp.database.DatabaseObject` fields

### 100% **Kotlin**

Just take a look to the source code and you will notice that is true, no JNI, dll, ...

## Use the database

### First step provide the application context

### Create a `fr.jhelp.database.DatabaseObject`

### Accepted fields type

### Constructor constraint

#### Primary keys

### Equals already defined

### `fr.jhelp.database.DatabaseNoSQL`

#### Store and delete  `fr.jhelp.database.DatabaseObject`

#### Do a query

##### Query **DSL**

##### Define conditions and table of possibilities

#### Some examples

