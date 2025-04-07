grep -nr . $1 | while read line ; do
    words=$(echo "$line" | tr -s ' ' | cut -d : -f 3-) # take the line, squeeze spaces, and cut out the grep headers
    words=${words// /$'\n'} #replace spaces with newlines (spaces were squeezed in the line above)
    a="$(echo "$words" | sort | uniq -c | sed 's/^\s*//'  | sort -rn)" #count occurences of each word and sort by the number of them


    # a="${a%%$'\n'*}"
    a=${a//$'\n'/ } #turn newlines back into spaces so I can use awk
    num=${a%% *} #takes everything before the first space (the number
    double=$(echo "$a" | awk '{print $2}')

    if [ $num -gt 1 ]; then
        echo "$double"
        echo "$line"
        echo ""
    fi

done