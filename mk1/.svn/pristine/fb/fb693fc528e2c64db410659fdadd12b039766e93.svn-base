grep -hr . $1 | tr -s ' ' '\n' | sort | uniq | while read word ; do
    echo "$word :"
    echo "$(grep -nrw $word $1  | sort | uniq)"
    echo ""

done