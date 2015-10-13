# Bolser Githooks

This works in the following method:-

The git branch should match the YouTrack ticket number. For example on YouTrack we use "connection-51". Therefore the git branch should contain the same. Usually something like "feature/connection-51-description-of-the-feature-or-bug".

This script will extract that and add it to the end of your commit message.

You can and should also add the time spend to the commit message in the following format:

* work 1d
* work 3h

Add this at the end of the commit message. It should look like this

```bash
git commit -m "This is the work we did work 1d"
```

This will then create a git commit looking like this

```bash
This the work we have done

YouTrack: #connection-51 work 1d```

and update YouTrack accordingly!
