# Github CLI

This very simple CL program bridges the gap between your console and (your)
Github content.

## Automated Actions

The idea is to embrace the power of your CL environment with automated actions
with Github data. For example, cloning all of your repositories is simple:

```
gli -u philcali -c /users/philcali/repos
Github Password > **************
Enter a Command > git clone {ssh_url} {name}
```

If you wanted someone else's public repos, another simple command:

```
gli -u philcali -c /users/username/repos
Github Password > **************
Enter a Command > git clone {git_url} {name}
```

The values in braces are dynamic values to be placed in some arbitrary command.

## The Program

(Note: this project is very young, so this is going to change)

```
gpf 1.0
Usage: gpf [options] <path>

  -u <value> | --username <value>
        Github username
  -c | --command
        Execute an OS command
  <path>
        Github API path
```

## Near Future

- object filters
- json config file argument (defaults ~/.glirc)
- cache results per path request for min 1 minute (option --no-cache)
- Clean up disgusting code (which is all of it)
- Consider making this a sbt plugin instead (more flexible code injections)
