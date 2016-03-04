### Updating

If there is an update to this repository you should navigate to where ever you cloned it to (perhaps ~/Sites/Githooks?) and run

```bash
git pull
```

Then run this command

```bash
sh bin/update-githooks.sh ~/Sites
```

Replace "~/Sites" with the location of all your work projects

That script will loop over all the folders in that directory, check for a .git folder, and run an update command

Updates should be few and far between, but it's worth knowing how to do this!
