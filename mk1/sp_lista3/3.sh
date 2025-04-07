mkdir -p /tmp/svn_script_3
svn checkout  -q -r $1 $2 /tmp/svn_script_3
grep -hr . --exclude-dir ".svn" /tmp/svn_script_3 | tr -s ' ' '\n' | sort | uniq | while read word ; do
    echo "$word :"
    echo "$(grep -rw --exclude-dir ".svn" $word /tmp/svn_script_3  | cut -f1 -d":" | sort | uniq | wc -l)"
#    echo ""

done

rm -rf /tmp/svn_script_3
