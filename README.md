# Guia-Hadoop-Project
# Proyecto de concurrencia 
-----
#### 📦 Paso a paso de la ejecucion del proyecto. 💻

## Download dataset
Encontramos nuestro dataset en [Kaggle](https://www.kaggle.com/stephangarland/ghtorrent-pull-requests)
Para este proyecto utilizamos el dataset que contiene los comentarios de github pull requests.

## Extracting important data
Lo primero que debemos hacer es extraer la columna del .csv que tiene la data que necesitamos
```python
import csv
from datetime import datetime

start_time = datetime.now()

with open(<<Path del .csv>>, 'rb') as f:
    reader = csv.reader(f)
    next(reader)  # Ignoramos nombre de la columna

    with open(<<Path y nombre del .txt donde queremos guardar la data>>, 'w') as nf:
        for row in reader:
            nf.write(row[0]+' ')

end_time = datetime.now()
print('Duration: {}'.format(end_time - start_time))
```

Este codigo puede ser adquirrido de los archivos subidos al portal.

-------

## Cleaning data 🧹
Para limpiar la data, debemos ejecutar el archivo de java llamado CleaningData, adjunto en los archivos subidos al portal, solo debemos cambiar algunas rutas.

```java
43          try {
44              File fichero = new File(<<Lugar donde queremos guardar la data limpia>>);
45              fw = new FileWriter(fichero);
46              bw = new BufferedWriter(fw);
```

```java
51      try {
52          BufferedReader br = new BufferedReader(new FileReader(<<Path del archivo donde almacenamos la data desde el script de python>>));
53          long total = 0;
54          while ((thisLine = br.readLine()) != null) {
```
```java
115    public static ArrayList<String> readPalabras() {
116        String fichero = "<<Path al archivo que contiene stopwords>>";
117        ArrayList<String> deleteWords = new ArrayList();
118        int cont = 0;
```
----

## Hadoop MAP&REDUCE
---
### Configuration Files

1 - Navegar a la carpeta 
```bash 
$ cd /Project_Hadoop/Project_Hadoop/Config/
```
Dentro del archivo subido al portal donde encontraremos los archivos ```core-site.mxl hdfs-site.xml yarn-site.xml mapred-site.xml```
2 - Seleccionar y copiar los cuatro archivos.

3 - Luego navegar en su sistema de archivos local a la carpeta donde tiene instalada su distribucion de hadoop, ingresar al directory donde se encuentran sus archivos de configuracion e.g.
```bash
$ cd home/user/hadoop-3.2.2/etc/hadoop/
``` 
4 - Pegar los archivos de configuracion copiados anteriormente. 

### Recordatorio Importante!

> Los archivos ```yarn-site.xml``` y ```mapred-site.xml``` estan configurados asumiendo que su sistema cuenta con 16GB libres de espacio de almacenamiento.

> Para que un programa de mapeo & reducción tenga tiempos de ejecución eficiente, la proporción de la memoria que se le asigna al trabajo de reducción debe ser el doble que la del trabajo de mapeo.

> La cantidad de memoria que se le asigna a heap de Java, es recomendable que sea de 1GB menos que lo que se le asignó al mapeo y a la reduccion(ya configurado en los archivos de brindados).

---

### Ejecucion de MapReduce

##### Start-up de HDFS
Navegue a la carpeta sbin localizado dentro de su distribucion de hadoop
```bash
$ cd /home/user/hadoop-3.2.2/sbin/
```

Para iniciar Hadoop Namenodes, Datanodes, Secondary Namenodes, ResourcesManagers y NodeManagers corra el siguiente comando: 
```bash
$ ./start-all.sh
```
##### Ejecucion de WordCount
Creamos la carpeta donde estará localizado el input en el HDFS (Hadoop
Distributed File System):
```bash
$ hadoop fs -mkdir /WordCount
```
Luego creamos la carpeta donde guardaremos los inputs
```bash
$ hadoop fs -mkdir /WordCount/Input
```
Despues agregamos el input file con la data limpiada que obtuvimos en los pasos posteriores "Extracting important Data" y "Cleaning Data", con el siguiente comando:
```bash
$ hadoop fs -put <<Direccion del archivo limpio>> <<Direccion de HDFS>>

e.g. :
$ hadoop fs -put /home/user/hadoop/cleanData.txt /WordCount/Input
```

Para comenzar el mappeo y el reduce, corra el siguiente comando: 
```bash
$ hadoop jar <<Direccion de .jar>> <<Nombre de JavaClass>> <<HDFS Input File>> <<HDFS Output File>>

e.g. :
$ hadoop jar /home/user/Project_Hadoop/WordCount/WordCountJAR.jar WordCount /WordCount/Input/cleanData.txt /WordCount/Output
```

Finalmente pasaremos el Output File generado por Hadoop al finalizar el proceso de MapReduce a nuestro sistema de archivos local, ejecutando el siguiente comando
```bash
$ hadoop fs -get <<Path HDFS de Outputput File>> <<Path en el localfilesystem donde queremos guardar el output file>>

e.g.:
$ hadoop fs -get /WordCount/Output/part-r-00000.txt /home/user/Project_Hadoop
```

##### Ejecucion de WordCount2Freq
Creamos la carpeta donde estará localizado el input en el HDFS (Hadoop
Distributed File System):
```bash
$ hadoop fs -mkdir /WordCount2Freq
```
Luego creamos la carpeta donde guardaremos los inputs
```bash
$ hadoop fs -mkdir /WordCount2Freq/Input
```
Despues agregamos el input file con la data limpiada que obtuvimos en los pasos posteriores "Extracting important Data" y "Cleaning Data", con el siguiente comando:
```bash
$ hadoop fs -put <<Direccion del archivo limpio>> <<Direccion de HDFS>>

e.g. :
$ hadoop fs -put /home/user/hadoop/cleanData.txt /WordCount2Freq/Input
```

Para comenzar el mappeo y el reduce, corra el siguiente comando: 
```bash
$ hadoop jar <<Direccion de .jar>> <<Nombre de JavaClass>> <<HDFS Input File>> <<HDFS Output File>>

e.g. :
$ hadoop jar /home/user/Project_Hadoop/WordCount2Freq/WC2Freq.jar WordCount /WordCount2Freq/Input/cleanData.txt /WordCount2Freq/Output
```

Finalmente pasaremos el Output File generado por Hadoop al finalizar el proceso de MapReduce a nuestro sistema de archivos local, ejecutando el siguiente comando
```bash
$ hadoop fs -get <<Path HDFS de Outputput File>> <<Path en el localfilesystem donde queremos guardar el output file>>

e.g.:
$ hadoop fs -get /WordCount2Freq/Output/part-r-00000.txt /home/user/Project_Hadoop
```
---

## Sorting data by descending frecuency orden 
### 1 Word Frequency

Utilizando los Output Files copiados de HDFS de WordCount y WordCount2Freq(part-r-00000.txt), ejecutamos los siguientes comandos: 
```bash
$ sort -k 2nr <<Path del txt que contiene el word count>> -o <<Nombre del nuevo file con resultados ordenados>>

e.g.:
$ sort -k 2nr /home/user/Project_Hadoop/part-r-00000.txt -o /home/user/Project_Hadoop/SortedWordCount.txt
```
### 2 Word Frequency
```bash
$ sort -k 4nr <<Path del txt que contiene el word count>> -o <<Nombre del nuevo file con resultados ordenados>>

e.g.:
$ sort -k 4nr /home/user/Project_Hadoop/part-r-00000.txt -o /home/user/Project_Hadoop/SortedWordCount2Freq.txt
```
Despues de estos dos pasos, nuestros archivos ya estan totalmente listos! Con un simple cat sortedfile.txt podremos ver la informacion sorteada en orden de mayor a menor por frecuencia.
```bash
$ cat /home/user/Project_Hadoop/SorteWordCount.txt
```

-sort : El comando de sorting.
-r : Para descending order.
-k : Para sortear por una columna en especifico.
2n : Para sortear por la segunda columna.
n  : Para indicar que es un valor numerico.
-------

## Frecuencia de las palabras 
### Para 1 Word frequency & 2 Words frequency 
Si deseamos obtener la frecuencia de las palabras o pares de palabras, debemos ejecutar el archivo de java llamado CountLines, adjunto en los archivos subidos al portal, solo debemos cambiar algunas rutas.

```java
23    public static void main(String[] args) throws FileNotFoundException, IOException {
24        // TODO code application logic here
25        File f1 = new File("<<Path del archivo que contiene el resultado del word count>>"); 
26        int linecount = 0;
27        File f2 = new File("<<Path del archivo que contiene el resultado del 2 words count>>"); 
28        int linecount2 = 0;
29        FileReader fr = new FileReader(f1);
```
Este codigo puede ser adquirrido de los archivos subidos al portal.

---------
