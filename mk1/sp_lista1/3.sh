grep -hr . $1 | tr -s ' ' '\n' | sort | uniq | while read word ; do
    echo "$word :"
    echo "$(grep -rw $word $1  | cut -f1 -d":" | sort | uniq | wc -l)"
    echo ""

done