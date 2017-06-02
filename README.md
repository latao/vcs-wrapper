# vcs-wrapper
vcs-wrapper

    使用范例
        public static void main(String[] args) throws VCSException {
            VCSContext vcsContext = new VCSContext("http://svn.teclick.com/arch", "account", "password");
            VCS vcs = VCSFactory.newInstance(VCSType.valueOf("svn"), vcsContext);
            vcs.checkout(new File("MyLocalFolder"), "project", "branch", "1024");
        }