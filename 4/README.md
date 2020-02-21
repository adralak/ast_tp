# Utilisation

## Importation dans Eclipse

Vous pouvez importer ce projet dans Eclipse en utilisant

	File > Import... > Existing Project into Workspace
	
Choisissez "Select root directory" puis "Browse..." pour sélectionner la racine
du projet.

## Compilation & Exécution

### Dans Eclipse

Vous pouvez faire un clic droit sur une des classes présente dans le dossier
`tests` pour ensuite sélectionner

	Run As... > Java Application
	
### Manuellement

Utilisez la commande `make` pour compiler le programme, puis

	$ java -cp minijava.jar:bin MyTest examples/rtl/BinaryTree.rtl
	
Pour lancer la classe `MyTest`.
Vous pouvez aussi directement utiliser le script `test` pour lancer la classe
`Test` :

	$ ./test examples/rtl/BinaryTree.rtl
	
## Outils

### Interpréteur MiniJava

Vous pouvez interpréter un programme MiniJava en utilisant la commande

	$ ./minijava file.java

### Compilateur MiniJava vers RTL

Vous pouvez compiler un programme MiniJava vers un fichier RTL avec la commande

	$ ./minijavac file.java > output.rtl
	
## Exemples

Des exemples de programmes MiniJava et RTL sont présents dans le dossier
`examples`.

## Nettoyage

Les classes sont compilées dans le dossier `bin`.
Vous pouvez utiliser la commande `make clean` pour effacer les fichiers
compilés.

## Archivage

Vous pouvez créer une archive de votre travail en utilisant la commande

	make archive
	
Le résultat est une archive `tp*.tar.gz` qui peut être supprimée avec la
commande `make mrproper`.
