### How it works

#### Branches

The git branch name should match the YouTrack ticket number. For example on YouTrack we use "connection-51". Therefore the git branch should contain the same. Usually something like "feature/connection-51-description-of-the-feature-or-bug".

This script will extract that and add it to the end of your commit message.


#### Time Tracking

You can and should also add the time spend to the commit message in the following format:

* time:1d
* time:3h
* time:6h

#### Type Tracking (Optional)

You may also add the type of work you have been doing by using the following format:

* type:development
* type:Testing
* type:documentation

You may capitalise the type if you wish, as shown above, but the script will capitalise for you

You do not need to use the type parameter. It will default to "Development" if left unused.

#### End product

Add this at the end of the commit message. It should look like this

```bash
git commit -m "This is the work we did time:1d"
```
OR

```bash
git commit -m "This is the work we did time:1d type:documentation"
```

This will then create a git commit looking like this

```bash
This the work we have done

YouTrack: #connection-51 work Documentation 1d
```

and update YouTrack accordingly!