import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitCommitMsg {

    private String mBranch;
    private String mMessage;
    private String mTimeSpent;

    private String WORK_PATTERN_STRING = "work{1}\\s{1}([0-9]{1,3}d)?([0-9]{1,3}h)?([0-9]{1,3}m)?";
    private String BRANCH_PATTERN_STRING = "[a-zA-Z]*-[0-9]{1,4}";
    private String BRANCH_PREFIX_PATTERN_STRING = "[a-zA-Z]*/";

    public GitCommitMsg() {
    }

    /**
     * Get the branch, if set
     *
     * @return The branch
     */
    public String getBranch() {
        return mBranch != null ? mBranch : "";
    }

    /**
     * Sets the branch name and number, if in a YouTrack format
     * e.g "connection-51"
     * <p>
     * Expected a format such as "feature/connection-51-this-is-our-branch-description"
     * </p>
     *
     * @param branch The branch in it's raw form
     */
    public void setBranch(String branch) {

        // Initialise the mBranch variable to an empty string
        mBranch = "";

        // Check if the branch contains a slash character
        // Having this is a sure sign we are using a properly formatted branch
        if (branch.contains("/")) {

            // Split on this slash
            String[] split = branch.split(BRANCH_PREFIX_PATTERN_STRING);

            // Make sure the length is greater than one. That we actually have something after the slash
            if (split.length > 1) {

                // Pattern matching.
                // We're looking for an number of value word characters
                // Split by a dash
                // Followed by any digits in 1 to 4 characters in length
                Pattern pattern = Pattern.compile(BRANCH_PATTERN_STRING);

                // Check the pattern against the split string
                // We're using the second split, everything after the slash
                Matcher matcher = pattern.matcher(split[1]);

                // If a match is found, we want the branch name to be the first group
                // A group is any number of identical matches... We probably only want one
                if (matcher.find()) {
                    mBranch = matcher.group(0);
                }
            }
        }
    }

    /**
     * Get the message, if set
     *
     * @return The commit message
     */
    public String getMessage() {
        return mMessage != null ? mMessage : "";
    }

    /**
     * Sets the message string without the time spent data
     * For use in the actual git commit message
     *
     * @param message The raw commit message
     */
    public void setMessage(String message) {
        // Initialised the mMessage variable
        mMessage = "";

        // Check to see if we have successfully set the time spent variable
        // If we have then we can split on it
        String timeSpent = getTimeSpent();
        if (!timeSpent.isEmpty()) {

            // Split the string on the time spent keyword
            // No need to use the whole regex for this, as we already know we can just use "work"
            // Since the time split variable is set
            String[] split = message.split("work");

            // If the split is larger than 1, then we have something to use
            if (split.length > 1) {

                // Use the first part of the split, the characters before the keyword
                // This should be the actual commit message before the time spent data
                mMessage = split[0];
            }
        } else {

            // If we don't have the work keyword, then we haven't added the time spent data
            // In which case, just give us the raw message as-is
            mMessage = message;
        }
    }

    /**
     * Get the time spent, if set
     *
     * @return The timespent
     */
    public String getTimeSpent() {
        return mTimeSpent != null ? mTimeSpent : "";
    }

    /**
     * Sets the time spent as isolated from the commit message
     *
     * @param message The raw commit message
     */
    public void setTimeSpent(String message) {

        // Initialise the mTimespent variable
        mTimeSpent = "";

        // Pattern matching for the time spent
        // We're looking for the word "work once
        // Followed by a whitespace character once
        // Finished by any digit between 0-9, either 1 to 3 times
        Pattern pattern = Pattern.compile(WORK_PATTERN_STRING);

        // Match the pattern against the message
        Matcher matcher = pattern.matcher(message);

        // If we find the pattern then set the first group to be the time spent
        if (matcher.find()) {

            mTimeSpent = matcher.group();
        }
    }

    /**
     * Builds the output commit message
     *
     * @return A structured commit message
     */
    public String getOutput() {

        // Create a new StringBuilder object
        StringBuilder output = new StringBuilder();

        // If either the branch or the time spent variables are empty
        // Then we either didn't use a proper YouTrack ticket for the branch name
        // Or we didn't add the time spent, so just return the message
        if (getBranch().isEmpty() || getTimeSpent().isEmpty()) {

            return getMessage();

        } else {
            // Build out commit message
            output.append(getMessage());
            output.append("\n\n");
            output.append("YouTrack: #");
            output.append(getBranch());
            output.append(" ");
            output.append(getTimeSpent());

            // Return it
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

        // Create an instance of this class
        GitCommitMsg gitCommitMsg = new GitCommitMsg();

        // First argument is the branch
        String branch = args[0];

        // First argument is the message
        String message = args[1];

        // Set the branch variable
        gitCommitMsg.setBranch(branch);

        // Set the time spent variable
        gitCommitMsg.setTimeSpent(message);

        // Set the message variable
        gitCommitMsg.setMessage(message);

        // Build the output string
        String output = gitCommitMsg.getOutput();

        // Return it
        System.out.println(output);
    }
}
