#!/bin/bash -eu

DIR="$1"
main()
{
	for files in "$DIR"/* ; do
       if [ -d "$files" ] ; then
          cd "$files"
          if [[ -e ".git" && -d ".git" ]] ; then
            echo "Updating $files"
            bash $(git config --path --get init.templatedir)/../update-hook.sh >> /dev/null
          fi
       fi
    done
}
main