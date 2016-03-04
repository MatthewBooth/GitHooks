import java.lang.Integer;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class GitCommitMsg {

    private final String WORK_STRING = "work";
    private final String YOUTRACK_STRING = "YouTrack: #";
    private final String TIME_STRING = "time:";
    private final String TYPE_STRING = "type:";
    private String mBranch;
    private String mMessage;
    private String mTimeSpent;
    private String mType;

    private String mOriginalMessage;

    private String mOriginalBranch;

    private String WORK_PATTERN_STRING = "time{1}:{1}([0-9]{1,3}d)?([0-9]{1,3}h)?([0-9]{1,3}m)?";

    private String WORK_PATTERN_INVALID_STRING = "time{1}:{1}" +
            "((([0-9]{1,3}h){1}([0-9]{1,3}d){1}){1}|" +
            "(([0-9]{1,3}m){1}([0-9]{1,3}d){1}){1}|" +
            "(([0-9]{1,3}m){1}([0-9]{1,3}h){1}){1}|" +
            "(([0-9]{1,3}m){1}([0-9]{1,3}d){1}){1})";

    private String BRANCH_PATTERN_STRING = "[a-zA-Z]*-[0-9]{1,4}";
    private String BRANCH_PREFIX_PATTERN_STRING = "[a-zA-Z]*/";

    private String TYPE_PATTERN_STRING = "(type:)([dD]evelopment{1}|[tT]esting{1}|[dD]ocumentation{1}){1}";

    private Integer EXIT_NORMALLY = 0;
    private Integer EXIT_INVALID_WORK_STRING = 1;

    private String NEW_LINE = "\n";
    private String SPACE = " ";

    public GitCommitMsg(String message, String branch) {
        // Set the original message
        setOriginalMessage(message);

        // Set the original branch
        setOriginalBranch(branch);

        // Set the branch variable
        setBranch(branch);

        // Set the time spent variable
        setTimeSpent(message);

        // Set the message variable
        setMessage(message);

        // Set the type variable
        setType(message);
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

        // Split the string on the time spent keyword
        // No need to use the whole regex for this, as we already know we can just use "time:"
        String[] split = message.split(TIME_STRING);

        // If the split is larger than 1, then we have something to use
        if (split.length > 1) {
            // Split again on the type String, just in case it is set before the time string
            if (split[0].contains(TYPE_STRING)) {
                split = message.split(TYPE_STRING);
            }

            // Use the first part of the split, the characters before the keyword
            // This should be the actual commit message before the time spent data
            mMessage = split[0];
        } else {
            mMessage = getOriginalMessage();
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
        // We're looking for the word "work" once
        // Followed by a colon character once
        // Finished by any digit between 0-9, either 1 to 3 times
        Pattern patternValid = Pattern.compile(WORK_PATTERN_STRING);

        // Match the pattern against the message
        Matcher matcherValid = patternValid.matcher(message);

        // Invalid pattern matching
        Pattern patternInvalid = Pattern.compile(WORK_PATTERN_INVALID_STRING);

        // Matcher for the invalid pattern
        Matcher matcherInvalid = patternInvalid.matcher(message);

        // Checks for an invalid pattern match. If we find one, then refuse the pattern
        // and return without setting the time
        if (!matcherInvalid.find()) {

            // If we find the pattern then set the first group to be the time spent
            if (matcherValid.find()) {

                // Stripe out the time: string
                String matched = matcherValid.group().replace(TIME_STRING, "");

                // Set the formatted string
                mTimeSpent = matched;
            }
        } else {
            System.exit(EXIT_INVALID_WORK_STRING);
        }
    }

    /**
     * Sets the type string
     *
     * @param String type  The string to set as the type
     */
    public void setType(String message) {
        // Initialise the mBranch variable to an empty string
        mType = "";

        // Pattern matching for the type
        Pattern patternValid = Pattern.compile(TYPE_PATTERN_STRING);

        // Match the pattern against the message
        Matcher matcherValid = patternValid.matcher(message);

        // If we find the pattern then set the first group to be the time spent
        if (matcherValid.find()) {
            // Stripe out the type: string
            String matched = matcherValid.group().replace(TYPE_STRING, "");

            // Capitalise the first letter
            String capitalise = matched.substring(0, 1).toUpperCase() + matched.substring(1);

            // Set the formatted string
            mType = capitalise;

        } else {
            mType = "Development";
        }
    }

    /**
     * Gets the type string
     *
     * @return String  The type string
     */
    public String getType() {
        return mType;
    }

    /**
     * Gets the original branch message as passed through via the bash script
     *
     * @return String the original branch string
     */
    public String getOriginalBranch() {
        return mOriginalBranch;
    }

    /**
     * Set the original branch string
     *
     * @param branch the original branch string
     */
    public void setOriginalBranch(String branch) {
        mOriginalBranch = branch;
    }

    /**
     * Get the original message string
     *
     * @return String  The original commit message
     */
    public String getOriginalMessage() {
        return mOriginalMessage;
    }

    /**
     * Set the original message string
     *
     * @param String message the message string
     */
    public void setOriginalMessage(String message) {
        mOriginalMessage = message;
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
            output.append(NEW_LINE + NEW_LINE);
            output.append(YOUTRACK_STRING);
            output.append(getBranch());
            output.append(SPACE);
            output.append(WORK_STRING);
            output.append(SPACE);
            output.append(getType());
            output.append(SPACE);
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

        // First argument is the branch
        String branch = args[0];

        // First argument is the message
        String message = args[1];

        // Create an instance of this class
        GitCommitMsg gitCommitMsg = new GitCommitMsg(message, branch);

        // Build the output string
        String output = gitCommitMsg.getOutput();

        // Return it
        System.out.println(output);
    }
}
