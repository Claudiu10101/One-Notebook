# One Notebook documentatie

"One Notebook" este o aplicatie de android facuta cu scopul de a organiza lectiile unui elev/student pentru a facilita invatarea pe viitor (sau copierea)

# Ecranul de pornire


Prima oara cand se deschide apllicatie o sa apara un ecran gol de genul:

![image](https://user-images.githubusercontent.com/79408463/210382843-aa5d4be9-b4d4-4c39-8dad-6f7d5b4ec48f.png)

Pentru a adauga o materie se apasa pe butonul "+" ne va duce sa scriem numele materiei pe care o dorim

![image](https://user-images.githubusercontent.com/79408463/210383337-c9a01ed2-56c0-4a59-b4e3-7a4d227b03c4.png)

Dupa ce s-a scris numele se apasa pe confirm si se poate observa ca s-a adaugat o materie noua. Acest lucru se poate face de cate ori este necesar

![image](https://user-images.githubusercontent.com/79408463/210383794-fc539302-8628-4e0b-9432-c917d831383f.png)

Daca vrem sa stergem o materie tinem apasat doua secunde si o sa fie stearsa.

# Cum s-a facut?
 
 De fiecare data cand se deschide ecranul de pornire se citeste dintr-un fisier din memoria interna numele tuturor materiilor si apoi sunt aranjate intr-o lista. Atunci cand apasam pe butonul "+" suntem mutati in alta activitate si cand apasam confirm adaugam un element nou in fisier si apoi ne intoarcem la ecranul de pornire citind iarasi din acel fisier.
 Atunci cand vrem sa stergem se trece prin fisier pana se gaseste elementul pe care vrem sa il stergem si apoi resetam ecranul.
 
 # Activitatea materie
 
 Daca apasam pe o materie o sa intram in alta activitate
 
 ![image](https://user-images.githubusercontent.com/79408463/210384847-09312c58-76a2-4436-83af-0d994e015bcf.png)
Apasand pe "+" se intampla acelasi lucru ca si pe ecranul cu materii.

#Lectia

Apasand pe una din lectii pe ecran apare o alta activitate care contine o poza si 3 butoane "next" si "previous" pentru navigarea prin poze si un buton cu o iconita de camera care apasand pe el ne muta pe aplicatia de camera pentru a face o poza.

![image](https://user-images.githubusercontent.com/79408463/229790787-67bf705c-12de-40fd-8278-a13ae16cc2e7.png)

#Cum s-a realizat

Fiecare lectie este un document de tip .txt care contine adresele pozelor din galerie astfel cand deschidem activitatea se citeste fisierul .txt, se afla adresele pozelor necesare, si se adauga intr-un vector, urmand ca apoi sa folosim butoanele "next" si "previous" pentru a itera prin vector

