import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitCommitMsg {

    private String mBranch;
    private String mMessage;
    private String mTimeSpent;

    public GitCommitMsg() {

    }

    public String getBranch() {
        return mBranch;
    }

    public void setBranch(String branch) {
        if (branch.contains("/")) {
            String[] split = branch.split("/");

            if (split.length > 1) {
                Pattern pattern = Pattern.compile("[a-zA-Z]*-[0-9]{1,4}");
                Matcher matcher = pattern.matcher(split[1]);

                if (matcher.find()) {
                    mBranch = matcher.group(0);
                } else {
                    mBranch = "";
                }
            } else {
                mBranch = "";
            }
        } else {
            mBranch = "";
        }
    }

    private String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {

        if (message.toLowerCase().contains("work")) {
            String[] split = message.split("work");
            if (split.length > 1) {
                mMessage = split[0];
            } else {
                mMessage = "";
            }
        } else {
            mMessage = message;
        }        
    }

    private String getTimeSpent() {
        return mTimeSpent;
    }

    private void setTimeSpent(String message) {

        Pattern pattern = Pattern.compile("work{1}\\s{1}[1-9]{1,3}[d|h|m]{1}");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            mTimeSpent = matcher.group();
        } else {
            mTimeSpent = "";
        }
    }

    private String getOutput() {

        StringBuilder output = new StringBuilder();

        if (getBranch().isEmpty() || getTimeSpent().isEmpty()) {
            return getMessage();
        } else {
            output.append(getMessage());
            output.append("\n\n");
            output.append("YouTrack: #");
            output.append(getBranch());
            output.append(" ");
            output.append(getTimeSpent());

            return output.toString();
        }
    }

    /**
     * Runs this program
     * @param args args[0] should be the branch via "git branch"
     *             args[1] should be the git commit-msg variable $1
     */
    public static void main(String[] args) {
        GitCommitMsg gitCommitMsg = new GitCommitMsg();

        gitCommitMsg.setBranch(args[0]);

        gitCommitMsg.setTimeSpent(args[1]);

        gitCommitMsg.setMessage(args[1]);

        System.out.println(gitCommitMsg.getOutput()); // Display the string.
    }
}
