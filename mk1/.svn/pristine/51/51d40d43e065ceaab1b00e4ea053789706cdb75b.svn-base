mkdir -p /tmp/svn_script_2
svn checkout -q -r $1 $2 /tmp/svn_script_2
echo "$(grep -hr --exclude-dir ".svn" . /tmp/svn_script_2 | tr -s ' ' '\n' | sort | uniq -c | sort -rn)"
rm -rf /tmp/svn_script_2
