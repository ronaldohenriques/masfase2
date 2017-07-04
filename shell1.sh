#!bin/bash
# echo -e "Descompacta todos arquivos .tar.bz do diretorio atual"
# for z in *.tar.bz;
# do 
# ((i++))
# tar -xvjf $z > $(printf "%03d" $i).txt;
# done 



COMP=./compress
UNCOMP=./uncompress

echo -e "Descompacta todos arquivos .tar.bz para um novo diretorio"
for z in *.tar.bz;
do 
tar -xvjf $z -C $UNCOMP
    # for file in $UNCOMP/*; do
    #     extension="${file##*.}"         
    #     new_name="${zip_filename}.${extension}"
    #     mv "${file}" "${new_name}"
    # done
done 


echo -e "renomeia todos arquivos descomptados para acrescentar-lhes extensao .txt"
for file in $UNCOMP/*; 
do
    extension="txt"         
    new_name="${file}.${extension}"
    mv "${file}" "${new_name}"
done

echo -e "Move os arquivo .tar.bz para um novo diretório"
mv *.tar.bz $COMP

echo -e "Criar um arquivo contendo a lista de arquivos descomptados .txt"
ls $UNCOMP/*.txt > lista1.txt

echo -e "Os dois comandos fazem o emso, listam os arquivos decompeactados e geram um .txt"
#ls $UNCOMP/*.txt | awk -F"/" '{print $NF}
#ls .$UNCOMP/*.txt | xargs -n 1 basename | cut -d '.' -f1 > lista2.txt

#for f in yoga*; 
#do mv $f $f.txt; 
#@done

