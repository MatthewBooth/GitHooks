import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitCommitMsg {

    private String mBranch;
    private String mMessage;
    private String mTimeSpent;

    public GitCommitMsg() {
    }

    /**
     * Get the branch, if set
     *
     * @return  The branch
     */
    public String getBranch() {
        return mBranch != null ? mBranch : "";
    }

    /**
     * Sets the branch name and number, if in a YouTrack format
     * e.g "connection-51"
     *
     * @param branch The branch in it's raw form
     */
    public void setBranch(String branch) {

        mBranch = "";

        if (branch.contains("/")) {

            String[] split = branch.split("/");

            if (split.length > 1) {

                Pattern pattern = Pattern.compile("[a-zA-Z]*-[0-9]{1,4}");
                Matcher matcher = pattern.matcher(split[1]);

                if (matcher.find()) {
                    mBranch = matcher.group(0);
                }
            }
        }
    }

    private String getMessage() {
        return mMessage != null ? mMessage : "";
    }

    /**
     * Sets the message string without the time spent data
     * For use in the actual git commit message
     *
     * @param message The raw commit message
     */
    public void setMessage(String message) {

        mMessage = "";

        String timeSpentKeyword = "work";

        if (message.toLowerCase().contains(timeSpentKeyword)) {

            String[] split = message.split(timeSpentKeyword);

            if (split.length > 1) {

                mMessage = split[0];
            }
        } else {

            mMessage = message;
        }
    }

    private String getTimeSpent() {
        return mTimeSpent != null ? mTimeSpent : "";
    }

    /**
     * Sets the time spent as isolated from the commit message
     *
     * @param message The raw commit message
     */
    private void setTimeSpent(String message) {

        mTimeSpent = "";

        Pattern pattern = Pattern.compile("work{1}\\s{1}[1-9]{1,3}[d|h|m]{1}");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {

            mTimeSpent = matcher.group();
        }
    }

    /**
     * Builds the output commit message
     *
     * @return A structured commit message
     */
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
     *
     * @param args args[0] should be the branch via "git branch"
     *             args[1] should be the git commit-msg
     */
    public static void main(String[] args) {
        GitCommitMsg gitCommitMsg = new GitCommitMsg();

        String branch = args[0];

        String message = args[1];

        gitCommitMsg.setBranch(branch);

        gitCommitMsg.setTimeSpent(message);

        gitCommitMsg.setMessage(message);

        String output = gitCommitMsg.getOutput();

        System.out.println(output);
    }
}
