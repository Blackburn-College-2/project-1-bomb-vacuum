# Setting Up Your 313 Repo

## Adding an SSH key to your GitHub

If using Windows, use Git Bash as the terminal. Right-click -> "Git Bash Here", now you have a
linux like terminal!

1. Have Git installed on the computer.
   - If using Windows, this will install Git Bash
   - When installing, default options are fine.
1. Check for existing ssh keys:
   1. Enter `ls -al ~/.ssh`
   1. If `id_rsa` and `id_rsa.pub` exist, then you already have a key made.
   continue onto the next step.
   1. If there is not a key, you will need to generate a new one.
      - Using your email associated with your GitHub account, enter `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`
        - When prompted for input, just press `Enter` to accept the defaults.
   1. Check if the ssh-agent is running
      - Enter `eval $(ssh-agent -s)` this should print something like `Agent pid 52486`
   1. Add your key to the ssh-agent
      - Enter `ssh-add ~/.shh/id_rsa`
1. Add the SSH key to your GitHub.
   1. Enter `cat ~/.ssh/id_rsa.pub`
   1. Highlight the entire output and copy using "right-click -> copy".
   1. Go to https://github.com/settings/profile and click the "SSH and GPG Keys" settings.
   1. Click "New SSH key". Give the key a meaningful title (ex. "Rlab Computer Tarkin") and paste
  the key.
      - Make sure the key starts with "ssh_rsa" and ends with your email.
   1. Click "Add SSH key"
1. Congrats! You have now added an SSH key to your GitHub account! You can repeat this process for any
trusted computer you want full access to your GitHub account.

Note: If using the Rlab/Plab machines on Ubuntu, you only need to do this once and the key will work
on any Rlab and Plab machine. If you're curious as to why this works, you can ask. Otherwise accept
that your life has been made slightly easier.

## Downloading your repo

1. go to https://github.com and sign in.
1. go to https://github.com/BlackburnCollege and find your first.last repo
1. Click "Clone or download"
   - Make sure you are using the SSH link (Click "Use SSH"/"Use HTTPS" to switch between the two).
1. Enter `git clone <repo-link>`
   - Example: `git clone git@github.com:BlackburnCollege/john.tuttle.git`
1. Enter `cd <repo-name>`
   - Example: `cd john.tuttle`
   - ***Note: This is the repo's root directory.***

## CS313 Setup

1. Add the following lines to the `.gitigore` file:
   - `*/**/nbproject`
   - `*/**/.idea`
   - `*/**/.gradle`
   
   If you do not have a `.gitignore` file, then create one at the repo's root directory.
1. Enter `mkdir cs313` and change into that directory.
1. Enter `mkdir sp20` and change into that directory.
1. Enter `mkdir written`
1. Enter `mkdir programs`
1. Create and save a temp file inside both the `programs` and `written` directorys.
   - Example: `echo "empty" > temp.txt`
1. Change back to the repo's root directory.
1. Enter `git add .`
   - This stages all files that have been created/modified/deleted to be committed
1. Enter `git commit -m "<message>"`
   - Example `git commit -m "added base folder structure"`
   - This saves the staged changes to the repo.
1. Enter `git push origin master`
   - This sends the current state of your saved repo to the remote repo (we use GitHub to store 
   our remote repos). More specifically to the `master` branch, which should be your default.

Your final file structure should look like:

```
john.tuttle
|-- cs313
|   |-- sp20
|   |   |-- written
|   |   |   |-- empty.txt
|   |   |-- programs
|   |   |   |-- empty.txt
|-- .gitignore
|-- (other folders/files left out for brevity)
```

## Turnin Process

### Written Assignments

1. Inside your repo's `cs313/sp20/written` directory, create a directory for the homework.
   - Typically this will be along the lines of `hw01`
2. Add your pdf submission to the appropriate homework folder.
3. Go back to the repo's root directory and run:
   - `git add .`
   - `git commit -m "<message>"` (message might be something like "turned in written hw01"
   - `git push`

```
john.tuttle
|-- cs313
|   |-- sp20
|   |   |-- written
|   |   |   |-- hw01-tuttle.pdf
```

### Program Assignments

Unless otherwise told, all program assignments should be Java Gradle Projects.

You will work on the assignment in your repo. Check the "creating-java-gradle-project.md" file
for instructions.

The program will need to follow a similar structure as written assignments.

```
john.tuttle
|-- cs313
|   |-- sp20
|   |   |-- programs
|   |   |   |-- hw01
|   |   |   |   | <gradle project>

