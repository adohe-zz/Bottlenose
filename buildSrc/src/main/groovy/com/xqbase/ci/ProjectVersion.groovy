package com.xqbase.ci

class ProjectVersion {
    final String major
    final String minor
    final String build
    final Boolean isSnapshot

    ProjectVersion(String major, String minor, String build, Boolean isSnapshot) {
        this.major = major
        this.minor = minor
        this.build = build
        this.isSnapshot = isSnapshot
    }

    @Override
    String toString() {
        String fullVersion = "$major.$minor"

        if (build) {
            fullVersion += ".$build"
        }
        
        if (isSnapshot) {
            fullVersion += "-SNAPSHOT"
        }
        
        fullVersion
    }
}
